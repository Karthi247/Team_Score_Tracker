import axios from 'axios';
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('tracker:token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const apiData = response => response.data?.data;
export const apiErrorMessage = error => error.response?.data?.message || error.response?.data?.error || error.message || 'The server request failed.';
export default api;
