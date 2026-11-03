import { useState } from 'react';
import { Link } from 'react-router-dom';
import { ChevronLeft, ChevronRight, Plus, Search, Trophy, Users, Pencil, Trash2, Volleyball } from 'lucide-react';
import { PageTitle, TeamLogo, Status, Empty } from '../../components/common/ui.jsx';
export { TeamsPage };
function TeamsPage({
  teams,
  openForm,
  remove
}) {
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('All');
  const [view, setView] = useState('grid');
  const filtered = teams.filter(t => t.name.toLowerCase().includes(search.toLowerCase()) && (status === 'All' || t.status === status));
  return <><PageTitle eyebrow="CLUB MANAGEMENT" title="Teams" description="Manage your squads, track performance, and build your club." action={<button className="btn btn-primary" onClick={() => openForm('team')}><Plus size={17} /> Add team</button>} /><div className="toolbar"><label className="search-box"><Search size={17} /><input className="search-input" value={search} onChange={e => setSearch(e.target.value)} placeholder="Search teams..." /><kbd>⌘ K</kbd></label><select className="filter-select" value={status} onChange={e => setStatus(e.target.value)}><option>All</option><option>Active</option><option>Inactive</option></select><div className="view-toggle"><button className={view === 'grid' ? 'selected' : ''} onClick={() => setView('grid')}>▦</button><button className={view === 'list' ? 'selected' : ''} onClick={() => setView('list')}>☷</button></div></div>
    {!filtered.length ? <Empty text="Try another search or add your first team." /> : <div className={view === 'grid' ? 'entity-grid' : 'entity-list'}>{filtered.map(t => <div className="entity-card team-card" key={t.id}><div className="entity-card-top"><TeamLogo name={t.name} teams={teams} /><button className="icon-btn more-btn" onClick={() => openForm('team', t)}><Pencil size={16} /></button></div><div className="entity-name"><Link to={`/teams/${t.id}`}>{t.name}</Link></div><div className="entity-sub">Led by {t.captain}</div><div className="entity-description">{t.description}</div><div className="entity-meta"><span><Users size={14} />{t.players} players</span><span><Volleyball size={14} />{t.played} played</span></div><div className="entity-card-foot"><Status>{t.status}</Status><span className="wins-label"><Trophy size={14} />{t.wins} wins</span><button className="delete-action" onClick={() => remove('team', t.id)} title="Delete team"><Trash2 size={15} /></button></div></div>)}</div>}
    <div className="list-foot">Showing <b>{filtered.length}</b> of <b>{teams.length}</b> teams <div className="pagination"><button disabled><ChevronLeft size={16} /></button><button className="page-current">1</button><button disabled><ChevronRight size={16} /></button></div></div></>;
}
