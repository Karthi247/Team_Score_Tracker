import { initials } from './format.js';

const titleCase = value => String(value ?? '').toLowerCase().replaceAll('_', ' ').replace(/\b\w/g, letter => letter.toUpperCase());
const enumValue = value => String(value ?? '').trim().toUpperCase().replaceAll(' ', '_').replaceAll('-', '_');
const mapTournamentStatus = value => ({ ONGOING: 'In progress' }[value] || titleCase(value));

export const fromTeam = team => ({
  ...team,
  short: initials(team.name),
  captain: team.captainName || '—',
  players: team.playerCount ?? 0,
  played: team.matchesPlayed ?? 0,
  wins: team.wins ?? 0,
  losses: team.losses ?? 0,
  color: team.logoUrl?.startsWith('#') ? team.logoUrl : '#397cf6',
  status: titleCase(team.status),
});

export const toTeamRequest = team => ({
  name: team.name,
  logoUrl: team.logoUrl || null,
  description: team.description || '',
  captainName: team.captain || team.captainName,
  status: enumValue(team.status || 'ACTIVE'),
});

export const fromPlayer = player => ({
  ...player,
  number: player.jerseyNumber,
  team: player.teamName,
  avatar: initials(player.name),
  status: titleCase(player.status),
});

export const toPlayerRequest = (player, teams) => ({
  name: player.name,
  jerseyNumber: Number(player.number),
  age: Number(player.age),
  position: player.position,
  teamId: Number(player.teamId || teams.find(team => team.name === player.team)?.id),
  profileImageUrl: player.profileImageUrl || null,
  status: enumValue(player.status || 'ACTIVE'),
});

export const fromTournament = tournament => ({
  ...tournament,
  sport: titleCase(tournament.sportType),
  start: tournament.startDate,
  end: tournament.endDate,
  teams: tournament.registeredTeamsCount ?? 0,
  maxTeams: tournament.maxTeams,
  status: mapTournamentStatus(tournament.status),
  format: titleCase(tournament.format),
});

export const toTournamentRequest = tournament => ({
  name: tournament.name,
  sportType: enumValue(tournament.sportType || tournament.sport),
  description: tournament.description || '',
  startDate: tournament.startDate || tournament.start,
  endDate: tournament.endDate || tournament.end,
  venue: tournament.venue || '',
  maxTeams: Number(tournament.maxTeams || tournament.teams || 8),
  status: tournament.status === 'In progress' ? 'ONGOING' : enumValue(tournament.status || 'UPCOMING'),
  format: enumValue(tournament.format || 'LEAGUE'),
});

export const fromMatch = match => ({
  ...match,
  home: match.teamAName,
  away: match.teamBName,
  homeId: match.teamAId,
  awayId: match.teamBId,
  homeScore: match.teamAScore ?? 0,
  awayScore: match.teamBScore ?? 0,
  tournament: match.tournamentName || 'Friendly',
  status: titleCase(match.status),
  date: `${match.matchDate}T${String(match.matchTime || '00:00').slice(0, 5)}`,
});

export const toMatchRequest = (match, teams, tournaments) => {
  const [matchDate, matchTime = '00:00'] = String(match.date || '').split('T');
  return {
    tournamentId: match.tournamentId || tournaments.find(item => item.name === match.tournament)?.id || null,
    teamAId: Number(match.teamAId || teams.find(item => item.name === match.home)?.id),
    teamBId: Number(match.teamBId || teams.find(item => item.name === match.away)?.id),
    matchDate,
    matchTime: matchTime.length === 5 ? `${matchTime}:00` : matchTime,
    venue: match.venue || '',
    roundName: match.roundName || null,
  };
};

export const fromStanding = item => ({
  ...item,
  id: item.teamId,
  name: item.teamName,
  played: item.matchesPlayed ?? 0,
  scored: item.pointsScored ?? 0,
  conceded: item.pointsConceded ?? 0,
  points: item.tournamentPoints ?? 0,
  short: initials(item.teamName),
});
