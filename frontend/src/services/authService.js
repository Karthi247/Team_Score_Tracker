import api from './api';
import { apiData } from './api';
export async function login(credentials) {
  const result = apiData(await api.post('/auth/login', credentials));
  localStorage.setItem('tracker:token', result.token);
  localStorage.setItem('tracker:user', result.fullName);
  return result;
}
export async function signup(profile) {
  const result = apiData(await api.post('/auth/signup', { ...profile, role: 'ORGANIZER' }));
  localStorage.setItem('tracker:token', result.token);
  localStorage.setItem('tracker:user', result.fullName);
  return result;
}
export async function getCurrentUser() { return apiData(await api.get('/auth/me')); }
export async function updateCurrentUser(profile) { return apiData(await api.put('/auth/profile', profile)); }
export function logout() { localStorage.removeItem('tracker:token'); localStorage.removeItem('tracker:user'); }
