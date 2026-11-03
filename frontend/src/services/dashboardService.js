import api from './api';
import { apiData } from './api';
export const getDashboard = async () => apiData(await api.get('/dashboard/statistics'));
export const getAnalytics = getDashboard;
