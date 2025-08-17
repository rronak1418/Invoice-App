import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'any'
})
export class InvoiceService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  getInvoices(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.apiUrl}/invoices?page=${page}&size=${size}`)
  }

  uploadCsv(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiUrl}/upload`, formData)
  }
}
