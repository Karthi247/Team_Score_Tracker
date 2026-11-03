import { LayoutDashboard, ShieldCheck, Users, Trophy, Volleyball, CirclePlay, Flag, ChartNoAxesCombined } from 'lucide-react';
export const navGroups = [{
  label: 'WORKSPACE',
  items: [['Overview', '/', LayoutDashboard], ['Teams', '/teams', ShieldCheck], ['Players', '/players', Users], ['Tournaments', '/tournaments', Trophy], ['Matches', '/matches', Volleyball], ['Live scoreboard', '/scoreboard', CirclePlay]]
}, {
  label: 'INSIGHTS',
  items: [['Leaderboard', '/leaderboard', Flag], ['Analytics', '/analytics', ChartNoAxesCombined]]
}];
;
