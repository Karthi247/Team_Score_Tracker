import api, { apiData } from './api.js';

export async function getStandings(tournamentId) {
  return apiData(await api.get(`/tournaments/${tournamentId}/standings`));
}
