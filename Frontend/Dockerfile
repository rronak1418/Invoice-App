# --------------------
# Stage 1: Build Angular app
# --------------------
FROM node:20-alpine AS builder

WORKDIR /app

# Install Angular CLI globally
RUN npm install -g @angular/cli@20.1.6

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source code
COPY . .

# Build SSR version of Angular app
RUN npm run build

# --------------------
# Stage 2: Serve with Node
# --------------------
FROM node:20-alpine

WORKDIR /app

# Copy built SSR app from builder
COPY --from=builder /app/dist/frontend ./dist/frontend

# Install production dependencies (Express)
COPY package*.json ./
RUN npm install --only=production

# Expose port (Angular SSR default)
EXPOSE 4000

# Start the app
CMD ["node", "dist/frontend/server/server.mjs"]
