import api from './api';
import { apiData } from './api';
export async function getPlayers(params = {}) { const data = apiData(await api.get('/players', { params: { size: 100, ...params } })); return data?.content ?? data ?? []; }
export const createPlayer = async player => apiData(await api.post('/players', player));
export const updatePlayer = async (id, player) => apiData(await api.put(`/players/${id}`, player));
export const deletePlayer = async id => apiData(await api.delete(`/players/${id}`));
