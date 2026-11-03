import { useState } from 'react';
import { Link } from 'react-router-dom';
import { CalendarDays, Plus, Search, SlidersHorizontal, Trophy, Users, Pencil, Trash2, Volleyball, MapPin } from 'lucide-react';
import { PageTitle, Status, Empty } from '../../components/common/ui.jsx';
import { fmtDate, initials } from '../../utils/format.js';
export { TournamentsPage };
function TournamentsPage({
  tournaments,
  openForm,
  remove
}) {
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('All');
  const filtered = tournaments.filter(t => t.name.toLowerCase().includes(search.toLowerCase()) && (status === 'All' || t.status === status));
  return <><PageTitle eyebrow="COMPETITIONS" title="Tournaments" description="Create competitions and follow every team’s journey." action={<button className="btn btn-primary" onClick={() => openForm('tournament')}><Plus size={17} /> Create tournament</button>} /><div className="toolbar"><label className="search-box"><Search size={17} /><input className="search-input" value={search} onChange={e => setSearch(e.target.value)} placeholder="Search tournaments..." /></label><select className="filter-select" value={status} onChange={e => setStatus(e.target.value)}><option>All</option><option>In progress</option><option>Upcoming</option><option>Completed</option></select><button className="btn btn-light filter-button"><SlidersHorizontal size={16} /> Filters</button></div>
    {!filtered.length ? <Empty text="No tournaments match your search." /> : <div className="entity-grid tournament-grid">{filtered.map((t, i) => <article className="entity-card tournament-card" key={t.id}><div className={`tournament-banner tournament-banner-${i % 3}`}><div className="banner-top"><span className="sport-tag"><Volleyball size={13} />{t.sport}</span><Status>{t.status}</Status></div><span className="banner-trophy"><Trophy size={44} /></span><span className="banner-initials">{initials(t.name)}</span></div><div className="tournament-content"><div className="entity-name"><Link to={`/tournaments/${t.id}`}>{t.name}</Link></div><div className="tournament-dates"><CalendarDays size={14} />{fmtDate(t.start)} <span>—</span> {fmtDate(t.end)}</div><div className="entity-meta"><span><Users size={14} />{t.teams} registered teams</span><span><MapPin size={14} />{t.venue}</span></div><div className="tournament-progress-label"><span>Registration progress</span><b>{Math.min(t.teams * 7, 94)}%</b></div><div className="progress-track"><i style={{
              width: `${Math.min(t.teams * 7, 94)}%`
            }} /></div><div className="entity-card-foot"><span className="format-label">{t.format} format</span><div className="row-actions"><button onClick={() => openForm('tournament', t)} title="Edit"><Pencil size={15} /></button><button onClick={() => remove('tournament', t.id)} title="Delete"><Trash2 size={15} /></button></div></div></div></article>)}</div>}<div className="list-foot">Showing <b>{filtered.length}</b> tournaments</div></>;
}
