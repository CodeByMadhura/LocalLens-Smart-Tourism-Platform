import axios from "axios";

// Base API URL — change this once if backend moves
const BASE_URL = "http://localhost:8080";

// Axios instance with auth header auto-injected
const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Inject JWT token into every request automatically
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("authToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth endpoints
export const AUTH_ENDPOINTS = {
  REGISTER: "/api/auth/register",
  VERIFY_EMAIL: "/api/auth/verify-email",
  RESEND_VERIFICATION: "/api/auth/resend-email-verification",
  SEND_OTP: "/api/auth/send-otp",
  VERIFY_PHONE_OTP: "/api/auth/verify-phone-otp",
  LOGIN_OTP: "/api/auth/login-otp",
};

export default api;
