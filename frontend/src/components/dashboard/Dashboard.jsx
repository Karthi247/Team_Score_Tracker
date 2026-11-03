import { Link } from 'react-router-dom';
import { ArrowUpRight, CalendarDays, Check, ChevronDown, Clock3, Plus, ShieldCheck, Trophy, Users, Volleyball, CirclePlay, MapPin } from 'lucide-react';
import { Area, AreaChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { PageTitle, Status, TeamLogo, Empty, QuickAction, ChartTooltip, teamColor } from '../common/ui.jsx';
import { dateLabel, initials } from '../../utils/format.js';
import { performanceData } from '../../data/statistics.js';
export { Dashboard };
function Dashboard({
  teams,
  players,
  tournaments,
  matches,
  openForm,
  dashboardData
}) {
  const cards = [['Total teams', dashboardData?.totalTeams ?? teams.length, 'Across all competitions', ShieldCheck, '#eff6ff', '#2563eb', 'Live total'], ['Total players', dashboardData?.totalPlayers ?? players.length, 'Registered athletes', Users, '#f2f0ff', '#7657d5', 'Live total'], ['Tournaments', dashboardData?.totalTournaments ?? tournaments.length, 'Across your club', Trophy, '#fff7e9', '#db8b21', 'Live total'], ['Total matches', dashboardData?.totalMatches ?? matches.length, 'This season', Volleyball, '#e9fbf5', '#0d9b70', 'Live total'], ['Live matches', dashboardData?.liveMatches ?? matches.filter(m => m.status === 'Live').length, 'Happening right now', CirclePlay, '#fff0ef', '#d95c52', 'Live now'], ['Completed', dashboardData?.completedMatches ?? matches.filter(m => m.status === 'Completed').length, 'Final results', Check, '#eff4ff', '#5376c4', 'This season']];
  const chartData = dashboardData?.teamPerformanceChart?.length ? dashboardData.teamPerformanceChart.map(point => ({ name: point.name, matches: point.value, wins: point.secondaryValue ?? 0 })) : performanceData;
  const topTeams = dashboardData?.topTeams?.length ? dashboardData.topTeams.map(team => ({ id: team.teamId, name: team.teamName, wins: team.wins, losses: team.losses, points: team.pointsScored })) : teams.slice(0, 4);
  const live = matches.find(x => x.status === 'Live');
  const upcoming = matches.filter(x => x.status === 'Upcoming').slice(0, 3);
  const recent = matches.filter(x => x.status !== 'Upcoming').slice(0, 4);
  return <><PageTitle eyebrow="MONDAY, JUNE 16, 2025" title="Good morning, Alex 👋" description="Here’s what’s happening across your club today." action={<button className="btn btn-primary" onClick={() => openForm('match')}><Plus size={17} /> Schedule a match</button>} />
    <div className="stat-grid">{cards.map(([name, value, sub, Icon, bg, color, delta]) => <div className="stat-card" key={name}><div className="stat-card-top"><span>{name}</span><span className="stat-icon" style={{
            background: bg,
            color
          }}><Icon size={19} /></span></div><div className="stat-value">{value}</div><div className="stat-foot"><span className="stat-delta"><ArrowUpRight size={13} />{delta}</span><span>{sub}</span></div></div>)}</div>
    <div className="dashboard-grid"><section className="panel performance-panel"><div className="panel-heading"><div><h2>Club performance</h2><p>Match activity and wins over time</p></div><button className="select-button">Last 7 months <ChevronDown size={14} /></button></div><div className="chart-legend"><span><i className="legend-blue" /> Matches</span><span><i className="legend-mint" /> Wins</span></div><div className="performance-chart"><ResponsiveContainer width="100%" height="100%"><AreaChart data={chartData} margin={{
              top: 10,
              right: 8,
              left: -22,
              bottom: 0
            }}><defs><linearGradient id="blueFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#397cf6" stopOpacity={0.16} /><stop offset="100%" stopColor="#397cf6" stopOpacity={0} /></linearGradient></defs><CartesianGrid stroke="#eff2f6" vertical={false} /><XAxis dataKey="name" tickLine={false} axisLine={false} tick={{
                fill: '#8b95a6',
                fontSize: 12
              }} dy={9} /><YAxis tickLine={false} axisLine={false} tick={{
                fill: '#8b95a6',
                fontSize: 12
              }} /><Tooltip content={<ChartTooltip />} /><Area type="monotone" dataKey="matches" name="Matches" stroke="#397cf6" strokeWidth={2.5} fill="url(#blueFill)" /><Area type="monotone" dataKey="wins" name="Wins" stroke="#21b58b" strokeWidth={2.5} fill="none" /></AreaChart></ResponsiveContainer></div></section>
      <section className="panel live-panel"><div className="panel-heading"><div><h2>Live match</h2><p>Premier League · Matchday 14</p></div><Status>Live</Status></div>{live ? <><div className="live-score-wrap"><div className="live-team"><TeamLogo name={live.home} teams={teams} /><b>{live.home}</b><span>HOME</span></div><div className="live-score"><strong>{live.homeScore}<i>:</i>{live.awayScore}</strong><span>67’</span></div><div className="live-team"><TeamLogo name={live.away} teams={teams} /><b>{live.away}</b><span>AWAY</span></div></div><Link className="text-action" to="/scoreboard">Open live scoreboard <ArrowUpRight size={15} /></Link></> : <Empty text="No matches are live right now." />}</section>
      <section className="panel recent-panel"><div className="panel-heading"><div><h2>Recent matches</h2><p>Latest results from your competitions</p></div><Link className="text-action" to="/matches">View all <ArrowUpRight size={15} /></Link></div><div className="match-table">{recent.map(m => <div className="match-row" key={m.id}><div className="match-date"><b>{dateLabel(m.date)}</b><span>{new Date(m.date).toLocaleTimeString('en-US', {
                  hour: 'numeric',
                  minute: '2-digit'
                })}</span></div><div className="match-teams"><div><span className="mini-crest" style={{
                  background: teamColor(m.home, teams)
                }}>{initials(m.home)}</span>{m.home}</div><div><span className="mini-crest" style={{
                  background: teamColor(m.away, teams)
                }}>{initials(m.away)}</span>{m.away}</div></div><div className="match-score">{m.homeScore} <i>–</i> {m.awayScore}</div><Status>{m.status}</Status></div>)}</div></section>
      <section className="panel upcoming-panel"><div className="panel-heading"><div><h2>Coming up</h2><p>Next on the fixture list</p></div><Link className="round-action" to="/matches"><ArrowUpRight size={16} /></Link></div>{upcoming.length ? upcoming.map(m => <div className="upcoming-item" key={m.id}><div className="calendar-tile"><b>{new Date(m.date).getDate()}</b><span>{new Date(m.date).toLocaleDateString('en-US', {
                month: 'short'
              }).toUpperCase()}</span></div><div className="upcoming-info"><b>{m.home} <span>vs</span> {m.away}</b><span><Clock3 size={13} />{new Date(m.date).toLocaleTimeString('en-US', {
                hour: 'numeric',
                minute: '2-digit'
              })} <i /> <MapPin size={13} />{m.venue}</span></div></div>) : <Empty text="No upcoming fixtures scheduled." />}<button className="add-fixture" onClick={() => openForm('match')}><Plus size={15} /> Add fixture</button></section>
    </div><div className="lower-grid"><div className="panel top-teams-panel"><div className="panel-heading"><div><h2>Top performing teams</h2><p>League standings this season</p></div><Link className="text-action" to="/leaderboard">Full table <ArrowUpRight size={15} /></Link></div><div className="team-list">{teams.slice(0, 4).map((team, i) => <div className="team-list-row" key={team.id}><span className={`rank rank-${i + 1}`}>{String(i + 1).padStart(2, '0')}</span><TeamLogo name={team.name} teams={teams} /><b>{team.name}</b><span className="team-record">{team.wins}W · {team.losses}L</span><div className="team-points"><strong>{team.points ?? team.wins * 3 + 2}</strong><span>pts</span></div></div>)}</div></div><div className="quick-actions panel"><div className="panel-heading"><div><h2>Quick actions</h2><p>Shortcuts to manage your club</p></div></div><div className="quick-action-grid"><QuickAction icon={ShieldCheck} label="Add a team" onClick={() => openForm('team')} /><QuickAction icon={Users} label="Add a player" onClick={() => openForm('player')} /><QuickAction icon={Trophy} label="New tournament" onClick={() => openForm('tournament')} /><QuickAction icon={CalendarDays} label="Schedule match" onClick={() => openForm('match')} /></div></div></div>
  </>;
}
