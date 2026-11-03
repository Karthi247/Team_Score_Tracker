import api from './api';
import { apiData } from './api';
export async function getMatches(params = {}) { const data = apiData(await api.get('/matches', { params: { size: 100, ...params } })); return data?.content ?? data ?? []; }
export async function getLiveMatches() { return apiData(await api.get('/matches/live')); }
export async function getRecentMatches(limit = 5) { return apiData(await api.get('/matches/recent', { params: { limit } })); }
export async function getUpcomingMatches(limit = 5) { return apiData(await api.get('/matches/upcoming', { params: { limit } })); }
export async function createMatch(match) { return apiData(await api.post('/matches', match)); }
export async function updateMatch(id, match) { return apiData(await api.put(`/matches/${id}`, match)); }
export async function deleteMatch(id) { return apiData(await api.delete(`/matches/${id}`)); }
export async function startMatch(id) { return apiData(await api.post(`/matches/${id}/start`)); }
export async function completeMatch(id) { return apiData(await api.post(`/matches/${id}/complete`)); }
export async function getMatchDetails(id) { return apiData(await api.get(`/matches/${id}/details`)); }
export async function getScoreEvents(id) { return apiData(await api.get(`/matches/${id}/scores`)); }
export async function addScoreEvent(id, event) { return apiData(await api.post(`/matches/${id}/scores`, event)); }
export async function deleteScoreEvent(id, eventId) { return apiData(await api.delete(`/matches/${id}/scores/${eventId}`)); }
