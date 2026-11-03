export const initialTeams = [{
  id: 1,
  name: 'Harbor Hawks',
  short: 'HH',
  color: '#2476f2',
  captain: 'Marcus Chen',
  players: 18,
  played: 12,
  wins: 9,
  losses: 2,
  status: 'Active',
  description: 'A fast, disciplined side from the waterfront district.'
}, {
  id: 2,
  name: 'City Strikers',
  short: 'CS',
  color: '#e95a4d',
  captain: 'Jordan Blake',
  players: 16,
  played: 12,
  wins: 8,
  losses: 3,
  status: 'Active',
  description: 'City pride, relentless pace, and a fearless attack.'
}, {
  id: 3,
  name: 'Northside United',
  short: 'NU',
  color: '#8c64df',
  captain: 'Avery Patel',
  players: 17,
  played: 11,
  wins: 7,
  losses: 3,
  status: 'Active',
  description: 'A community team built around teamwork and composure.'
}, {
  id: 4,
  name: 'Riverside Rovers',
  short: 'RR',
  color: '#19a887',
  captain: 'Sam Rivera',
  players: 15,
  played: 12,
  wins: 6,
  losses: 4,
  status: 'Active',
  description: 'Resilient competitors with a strong defensive record.'
}, {
  id: 5,
  name: 'East End Eagles',
  short: 'EE',
  color: '#ee9c3b',
  captain: 'Taylor Morgan',
  players: 16,
  played: 10,
  wins: 5,
  losses: 4,
  status: 'Active',
  description: 'Young talent and high energy from the East End.'
}, {
  id: 6,
  name: 'Summit Wolves',
  short: 'SW',
  color: '#3e526e',
  captain: 'Riley Johnson',
  players: 14,
  played: 9,
  wins: 4,
  losses: 4,
  status: 'Inactive',
  description: 'A tactical side always looking for the next challenge.'
}];
export const initialPlayers = [{
  id: 1,
  name: 'Marcus Chen',
  team: 'Harbor Hawks',
  number: 7,
  position: 'Forward',
  age: 24,
  status: 'Active',
  avatar: 'MC'
}, {
  id: 2,
  name: 'Olivia Parker',
  team: 'City Strikers',
  number: 10,
  position: 'Midfielder',
  age: 22,
  status: 'Active',
  avatar: 'OP'
}, {
  id: 3,
  name: 'Avery Patel',
  team: 'Northside United',
  number: 4,
  position: 'Defender',
  age: 26,
  status: 'Active',
  avatar: 'AP'
}, {
  id: 4,
  name: 'Sam Rivera',
  team: 'Riverside Rovers',
  number: 1,
  position: 'Goalkeeper',
  age: 25,
  status: 'Active',
  avatar: 'SR'
}, {
  id: 5,
  name: 'Taylor Morgan',
  team: 'East End Eagles',
  number: 11,
  position: 'Forward',
  age: 21,
  status: 'Injured',
  avatar: 'TM'
}, {
  id: 6,
  name: 'Noah Williams',
  team: 'Harbor Hawks',
  number: 8,
  position: 'Midfielder',
  age: 23,
  status: 'Active',
  avatar: 'NW'
}, {
  id: 7,
  name: 'Mia Thompson',
  team: 'City Strikers',
  number: 3,
  position: 'Defender',
  age: 27,
  status: 'Active',
  avatar: 'MT'
}];
export const initialTournaments = [{
  id: 1,
  name: 'Premier League 2025',
  sport: 'Football',
  start: '2025-04-12',
  end: '2025-08-24',
  venue: 'Central Sports Arena',
  teams: 12,
  status: 'In progress',
  format: 'League'
}, {
  id: 2,
  name: 'Summer Cup',
  sport: 'Football',
  start: '2025-06-01',
  end: '2025-06-28',
  venue: 'Riverside Grounds',
  teams: 8,
  status: 'Upcoming',
  format: 'Knockout'
}, {
  id: 3,
  name: 'Community Shield',
  sport: 'Football',
  start: '2025-02-10',
  end: '2025-03-16',
  venue: 'East End Park',
  teams: 10,
  status: 'Completed',
  format: 'Round Robin'
}];
export const initialMatches = [{
  id: 1,
  home: 'Harbor Hawks',
  away: 'City Strikers',
  homeScore: 2,
  awayScore: 1,
  tournament: 'Premier League 2025',
  date: '2025-06-15T16:30',
  venue: 'Central Arena',
  status: 'Live'
}, {
  id: 2,
  home: 'Northside United',
  away: 'Riverside Rovers',
  homeScore: 3,
  awayScore: 2,
  tournament: 'Premier League 2025',
  date: '2025-06-15T14:00',
  venue: 'Northside Field',
  status: 'Completed'
}, {
  id: 3,
  home: 'East End Eagles',
  away: 'Summit Wolves',
  homeScore: 0,
  awayScore: 0,
  tournament: 'Summer Cup',
  date: '2025-06-17T18:00',
  venue: 'East End Park',
  status: 'Upcoming'
}, {
  id: 4,
  home: 'Harbor Hawks',
  away: 'Northside United',
  homeScore: 0,
  awayScore: 0,
  tournament: 'Premier League 2025',
  date: '2025-06-19T17:00',
  venue: 'Central Arena',
  status: 'Upcoming'
}, {
  id: 5,
  home: 'City Strikers',
  away: 'East End Eagles',
  homeScore: 1,
  awayScore: 0,
  tournament: 'Premier League 2025',
  date: '2025-06-12T16:00',
  venue: 'City Stadium',
  status: 'Completed'
}];
export const standings = initialTeams.slice(0, 5).map((team, i) => ({
  ...team,
  played: 12 - (i === 2 ? 1 : 0),
  draws: i === 0 ? 1 : 2,
  scored: 28 - i * 3,
  conceded: 12 + i * 2,
  points: 28 - i * 3
}));
