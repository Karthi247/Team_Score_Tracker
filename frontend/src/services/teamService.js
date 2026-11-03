import api from './api';
import { apiData } from './api';
export async function getTeams(params = {}) { const data = apiData(await api.get('/teams', { params: { size: 100, ...params } })); return data?.content ?? data ?? []; }
export const createTeam = async team => apiData(await api.post('/teams', team));
export const updateTeam = async (id, team) => apiData(await api.put(`/teams/${id}`, team));
export const deleteTeam = async id => apiData(await api.delete(`/teams/${id}`));
export const getTeamStatistics = async id => apiData(await api.get(`/teams/${id}/statistics`));
