import { Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { HttpClientModule, HttpResponse } from '@angular/common/http';
import { InvoiceService } from './services/invoice.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CurrencyPipe,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatInputModule,
    HttpClientModule,
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App implements OnInit {
  displayedColumns: string[] = ['customerId', 'invoiceNumber', 'invoiceDate', 'description', 'amount', 'ageInDays'];
  dataSource = new MatTableDataSource<any>([]);
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild('fileInput', { static: false }) fileInput!: ElementRef<HTMLInputElement>;

  constructor(private invoiceService: InvoiceService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadInvoices();
    setTimeout(() => {
      console.log('fileInput:', this.fileInput);
    }, 0);
  }

loadInvoices() {
  this.invoiceService.getInvoices(this.pageIndex, this.pageSize).subscribe({
    next: (page) => {
      this.dataSource.data = [
        ...page.content.map((invoice: any) => ({
          ...invoice,
          ageInDays: this.calculateAgeInDays(invoice.invoiceDate)
        }))
      ];
      this.totalElements = page.totalElements;
      this.cdr.detectChanges();
    },
    error: (err) => console.error('Error loading invoices', err)
  });
}

  calculateAgeInDays(invoiceDate: string): number {
    const today = new Date();
    const invDate = new Date(invoiceDate);
    const diffTime = Math.abs(today.getTime() - invDate.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadInvoices();
  }

  triggerFileInput() {
    if (this.fileInput && this.fileInput.nativeElement) {
      this.fileInput.nativeElement.click();
    } else {
      console.error('File input element is not available');
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.invoiceService.uploadCsv(file).subscribe({
        next: () => {
          this.loadInvoices();
          this.pageIndex = 0;
          this.fileInput.nativeElement.value = '';
          alert('File uploaded successfully');
        },
        error: (error) => {
          console.error('Error uploading file', error);
          if (error.status !== 200 && error.status !== 201) {
            alert("Error uploading the file");
          }
        }
    });
    }
  }
}
