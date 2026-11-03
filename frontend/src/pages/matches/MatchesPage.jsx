import { useState } from 'react';
import { Link } from 'react-router-dom';
import { CalendarDays, Check, Clock3, Plus, Search, Trophy, Eye, Pencil, Trash2, Volleyball, MapPin } from 'lucide-react';
import { PageTitle, Status, Empty, TeamLogo } from '../../components/common/ui.jsx';
import { fmtDate } from '../../utils/format.js';
export { MatchesPage };
function MatchesPage({
  matches,
  teams,
  tournaments,
  openForm,
  remove
}) {
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('All');
  const filtered = matches.filter(m => (m.home + m.away + m.tournament).toLowerCase().includes(search.toLowerCase()) && (status === 'All' || m.status === status));
  return <><PageTitle eyebrow="FIXTURES & RESULTS" title="Matches" description="Every fixture, result, and live moment from your club." action={<button className="btn btn-primary" onClick={() => openForm('match')}><Plus size={17} /> Schedule match</button>} /><div className="match-stats"><div><span className="match-stat-icon blue"><Volleyball size={17} /></span><span><b>{matches.length}</b><small>Total matches</small></span></div><div><span className="match-stat-icon green"><Check size={17} /></span><span><b>{matches.filter(m => m.status === 'Completed').length}</b><small>Completed</small></span></div><div><span className="match-stat-icon amber"><Clock3 size={17} /></span><span><b>{matches.filter(m => m.status === 'Upcoming').length}</b><small>Upcoming</small></span></div><div><span className="match-stat-icon red"><span className="pulsing-dot" /></span><span><b>{matches.filter(m => m.status === 'Live').length}</b><small>Live now</small></span></div></div><div className="toolbar"><label className="search-box"><Search size={17} /><input className="search-input" value={search} onChange={e => setSearch(e.target.value)} placeholder="Search teams or tournaments..." /></label><select className="filter-select" value={status} onChange={e => setStatus(e.target.value)}><option>All</option><option>Live</option><option>Upcoming</option><option>Completed</option></select><select className="filter-select"><option>All tournaments</option>{tournaments.map(t => <option key={t.id}>{t.name}</option>)}</select></div><div className="match-list">{filtered.map(m => <div className="panel match-card" key={m.id}><div className="match-card-top"><span><Trophy size={14} />{m.tournament}</span><Status>{m.status}</Status></div><div className="match-card-main"><div className="match-side"><TeamLogo name={m.home} teams={teams} /><div><b>{m.home}</b><span>Home</span></div></div><div className="match-result">{m.status === 'Upcoming' ? <span className="vs-label">VS</span> : <b>{m.homeScore}<i>:</i>{m.awayScore}</b>}<span>{m.status === 'Live' ? '67’' : fmtDate(m.date)}</span></div><div className="match-side away"><TeamLogo name={m.away} teams={teams} /><div><b>{m.away}</b><span>Away</span></div></div></div><div className="match-card-foot"><span><CalendarDays size={14} />{new Date(m.date).toLocaleString('en-US', {
              month: 'short',
              day: 'numeric',
              hour: 'numeric',
              minute: '2-digit'
            })}</span><span><MapPin size={14} />{m.venue}</span><div className="row-actions"><Link to={`/matches/${m.id}`} title="View match"><Eye size={15} /></Link><button onClick={() => openForm('match', m)} title="Edit"><Pencil size={15} /></button><button onClick={() => remove('match', m.id)} title="Delete"><Trash2 size={15} /></button></div></div></div>)}{!filtered.length && <Empty text="Schedule a match or update your filters." />}</div></>;
}
