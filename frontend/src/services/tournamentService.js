import api from './api';
import { apiData } from './api';
export async function getTournaments(params = {}) { const data = apiData(await api.get('/tournaments', { params: { size: 100, ...params } })); return data?.content ?? data ?? []; }
export const createTournament = async tournament => apiData(await api.post('/tournaments', tournament));
export const updateTournament = async (id, tournament) => apiData(await api.put(`/tournaments/${id}`, tournament));
export const deleteTournament = async id => apiData(await api.delete(`/tournaments/${id}`));
export const getTournamentTeams = async id => apiData(await api.get(`/tournaments/${id}/teams`));
export const getTournamentStatistics = async id => apiData(await api.get(`/tournaments/${id}/statistics`));
