import { ArrowUpRight, Download, Trophy, Volleyball, Zap, TrendingUp } from 'lucide-react';
import { Area, AreaChart, Bar, BarChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { initialTeams, standings } from '../../data/mockData.js';
import { PageTitle, TeamLogo, ChartTooltip } from '../../components/common/ui.jsx';
import { performanceData } from '../../data/statistics.js';
export { AnalyticsPage };
function AnalyticsPage({ dashboardData }) {
  const topTeams = dashboardData?.topTeams?.length ? dashboardData.topTeams.map(team => ({ ...team, name: team.teamName || team.name, scored: team.pointsScored ?? team.scored })) : standings;
  const winData = topTeams.map(team => ({
    name: team.teamName || team.name,
    win: team.winRate ?? Math.round((team.wins || 0) / Math.max(team.matchesPlayed || team.played || 1, 1) * 100)
  }));
  const resultColors = { COMPLETED: '#22b587', COMPLETED_MATCHES: '#22b587', UPCOMING: '#eab34b', LIVE: '#e86b6b', WINS: '#22b587', DRAWS: '#eab34b', LOSSES: '#e86b6b' };
  const results = dashboardData?.matchStatusChart?.length ? dashboardData.matchStatusChart.map(point => ({ name: point.name, value: Number(point.value), color: resultColors[String(point.name).toUpperCase().replaceAll(' ', '_')] || '#7894c6' })) : [{
    name: 'Wins',
    value: 68,
    color: '#22b587'
  }, {
    name: 'Draws',
    value: 19,
    color: '#eab34b'
  }, {
    name: 'Losses',
    value: 13,
    color: '#e86b6b'
  }];
  const matchesTotal = dashboardData?.totalMatches ?? 148;
  const pointsTotal = topTeams.reduce((sum, team) => sum + (team.pointsScored ?? team.scored ?? 0), 0) || 326;
  const averageWinRate = winData.length ? (winData.reduce((sum, team) => sum + team.win, 0) / winData.length).toFixed(1) : '72.8';
  const performance = dashboardData?.teamPerformanceChart?.length ? dashboardData.teamPerformanceChart.map(point => ({ name: point.name, matches: point.value, wins: point.secondaryValue ?? 0 })) : performanceData;
  return <><PageTitle eyebrow="CLUB INSIGHTS" title="Analytics" description="A closer look at performance across your club." action={<button className="btn btn-light" onClick={() => window.print()}><Download size={16} /> Export report</button>} /><div className="analytics-kpis"><div className="panel analytics-kpi"><span className="analytics-kpi-icon mint"><TrendingUp size={18} /></span><div><small>AVERAGE WIN RATE</small><b>{averageWinRate}%</b><span className="positive"><ArrowUpRight size={14} /> 4.2% this season</span></div></div><div className="panel analytics-kpi"><span className="analytics-kpi-icon lavender"><Volleyball size={18} /></span><div><small>MATCHES THIS SEASON</small><b>{matchesTotal}</b><span>Across all competitions</span></div></div><div className="panel analytics-kpi"><span className="analytics-kpi-icon peach"><Zap size={18} /></span><div><small>TOTAL GOALS SCORED</small><b>{pointsTotal}</b><span>2.2 goals per match</span></div></div><div className="panel analytics-kpi"><span className="analytics-kpi-icon sky"><Trophy size={18} /></span><div><small>ACTIVE COMPETITIONS</small><b>{dashboardData?.totalTournaments ?? 3}</b><span>From your server</span></div></div></div><div className="analytics-grid"><div className="panel chart-panel"><div className="panel-heading"><div><h2>Team win percentage</h2><p>Win rate by team this season</p></div></div><div className="chart-box"><ResponsiveContainer width="100%" height="100%"><BarChart data={winData} margin={{
              top: 10,
              right: 5,
              left: -20,
              bottom: 0
            }}><CartesianGrid stroke="#edf0f5" vertical={false} /><XAxis dataKey="name" tickLine={false} axisLine={false} tick={{
                fill: '#8893a5',
                fontSize: 12
              }} /><YAxis tickLine={false} axisLine={false} unit="%" tick={{
                fill: '#8893a5',
                fontSize: 12
              }} /><Tooltip content={<ChartTooltip />} /><Bar dataKey="win" name="Win rate" fill="#397cf6" radius={[5, 5, 0, 0]} barSize={34} /></BarChart></ResponsiveContainer></div></div><div className="panel chart-panel"><div className="panel-heading"><div><h2>Match results</h2><p>Season result distribution</p></div></div><div className="donut-wrap"><ResponsiveContainer width="58%" height="100%"><PieChart><Pie data={results} dataKey="value" innerRadius={61} outerRadius={84} paddingAngle={5} stroke="none">{results.map(r => <Cell key={r.name} fill={r.color} />)}</Pie><Tooltip /></PieChart></ResponsiveContainer><div className="donut-total"><b>{matchesTotal}</b><span>matches</span></div><div className="donut-legend">{results.map(r => <div key={r.name}><i style={{
                background: r.color
              }} /><span>{r.name}</span><b>{r.value}%</b></div>)}</div></div></div><div className="panel chart-panel wide-chart"><div className="panel-heading"><div><h2>Monthly match statistics</h2><p>Matches played and won throughout the season</p></div><span className="chart-legend"><i className="legend-blue" /> Matches <i className="legend-mint" /> Wins</span></div><div className="chart-box"><ResponsiveContainer width="100%" height="100%"><AreaChart data={performance} margin={{
              top: 10,
              right: 8,
              left: -22,
              bottom: 0
            }}><CartesianGrid stroke="#edf0f5" vertical={false} /><XAxis dataKey="name" tickLine={false} axisLine={false} tick={{
                fill: '#8893a5',
                fontSize: 12
              }} /><YAxis tickLine={false} axisLine={false} tick={{
                fill: '#8893a5',
                fontSize: 12
              }} /><Tooltip content={<ChartTooltip />} /><Area type="monotone" dataKey="matches" name="Matches" stroke="#397cf6" strokeWidth={2.5} fill="#eff5ff" /><Area type="monotone" dataKey="wins" name="Wins" stroke="#21b58b" strokeWidth={2.5} fill="none" /></AreaChart></ResponsiveContainer></div></div><div className="panel top-scorers"><div className="panel-heading"><div><h2>Top performing teams</h2><p>Most points this season</p></div></div>{standings.slice(0, 4).map((t, i) => <div className="scorer-row" key={t.id}><span className={`rank rank-${i + 1}`}>{i + 1}</span><TeamLogo name={t.name} teams={initialTeams} /><b>{t.name}</b><strong>{t.scored}<small> PF</small></strong></div>)}</div></div></>;
}
