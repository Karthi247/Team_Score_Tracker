import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowUpRight, Trophy, Users, Pencil, Volleyball, ArrowLeft, TrendingUp } from 'lucide-react';
import { TeamLogo, Status, Metric, NotFound } from '../../components/common/ui.jsx';
import { PlayerRows } from '../players/PlayerRows.jsx';
import { fmtDate } from '../../utils/format.js';
export { TeamDetail };
function TeamDetail({
  teams,
  players,
  matches,
  openForm,
  notify
}) {
  const {
    id
  } = useParams();
  const team = teams.find(t => t.id === Number(id));
  const [tab, setTab] = useState('Overview');
  if (!team) return <NotFound label="Team" />;
  const roster = players.filter(p => p.team === team.name);
  return <><Link to="/teams" className="back-link"><ArrowLeft size={15} /> Back to teams</Link><div className="detail-hero"><TeamLogo name={team.name} teams={teams} size="large" /><div className="detail-title"><div className="eyebrow">TEAM PROFILE <span>·</span> EST. 2018</div><h1>{team.name}</h1><p>{team.description}</p></div><div className="detail-actions"><Status>{team.status}</Status><button className="btn btn-light" onClick={() => openForm('team', team)}><Pencil size={15} /> Edit team</button></div></div><div className="detail-metrics"><Metric label="Players" value={team.players} icon={Users} /><Metric label="Matches played" value={team.played} icon={Volleyball} /><Metric label="Wins" value={team.wins} icon={Trophy} /><Metric label="Win percentage" value={`${Math.round(team.wins / team.played * 100)}%`} icon={TrendingUp} /></div><div className="tabs">{['Overview', 'Players', 'Match history', 'Statistics'].map(t => <button className={tab === t ? 'tab-active' : ''} onClick={() => setTab(t)} key={t}>{t}</button>)}</div>{tab === 'Players' ? <div className="panel table-panel"><div className="panel-heading"><div><h2>Squad roster</h2><p>{roster.length} players registered</p></div><button className="btn btn-light" onClick={() => notify('Use the Players page to manage your roster.')}><Users size={15} /> Manage roster</button></div><PlayerRows players={roster} teams={teams} /></div> : tab === 'Match history' ? <div className="panel table-panel"><div className="panel-heading"><div><h2>Match history</h2><p>Recent team results</p></div></div>{matches.filter(m => m.home === team.name || m.away === team.name).map(m => <div className="simple-row" key={m.id}><b>{m.home} {m.homeScore} – {m.awayScore} {m.away}</b><span>{fmtDate(m.date)}</span><Status>{m.status}</Status></div>)}</div> : <div className="dashboard-grid detail-grid"><div className="panel"><div className="panel-heading"><div><h2>About the team</h2><p>Club details and squad information</p></div></div><div className="about-grid"><div><small>CAPTAIN</small><b>{team.captain}</b></div><div><small>TEAM STATUS</small><Status>{team.status}</Status></div><div><small>PLAYERS</small><b>{team.players} registered</b></div><div><small>SEASON RECORD</small><b>{team.wins}W – {team.losses}L</b></div></div></div><div className="panel"><div className="panel-heading"><div><h2>Squad leaders</h2><p>Key players for this team</p></div><Link className="text-action" to="/players">View roster <ArrowUpRight size={15} /></Link></div>{roster.slice(0, 3).map(p => <div className="leader-row" key={p.id}><span className="avatar player-avatar">{p.avatar}</span><div><b>{p.name}</b><small>{p.position} · #{p.number}</small></div><Trophy size={16} /></div>)}</div></div>}</>;
}
