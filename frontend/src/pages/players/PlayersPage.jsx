import { useState } from 'react';
import { ChevronLeft, ChevronRight, Download, Plus, Search, Users, Pencil, Trash2 } from 'lucide-react';
import { PageTitle, Status, Empty, teamColor } from '../../components/common/ui.jsx';
export { PlayersPage };
function PlayersPage({
  players,
  teams,
  openForm,
  remove
}) {
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('All');
  const filtered = players.filter(p => (p.name + p.team + p.position).toLowerCase().includes(search.toLowerCase()) && (filter === 'All' || p.team === filter));
  return <><PageTitle eyebrow="CLUB MANAGEMENT" title="Players" description="Your athletes, all in one place. Keep your roster up to date." action={<button className="btn btn-primary" onClick={() => openForm('player')}><Plus size={17} /> Add player</button>} /><div className="player-summary"><span><Users size={17} /> <b>{players.length}</b> registered players</span><span><i /> {players.filter(p => p.status === 'Active').length} active this season</span></div><div className="toolbar"><label className="search-box"><Search size={17} /><input className="search-input" value={search} onChange={e => setSearch(e.target.value)} placeholder="Search by name, team or position..." /></label><select className="filter-select" value={filter} onChange={e => setFilter(e.target.value)}><option value="All">All teams</option>{teams.map(t => <option key={t.id}>{t.name}</option>)}</select><button className="btn btn-light filter-button"><Download size={16} /> Export</button></div>
    <div className="panel table-panel"><table className="data-table"><thead><tr><th>PLAYER</th><th>TEAM</th><th>JERSEY</th><th>POSITION</th><th>AGE</th><th>STATUS</th><th></th></tr></thead><tbody>{filtered.map((p, i) => <tr key={p.id}><td><div className="player-cell"><span className={`avatar player-avatar tone-${i % 5}`}>{p.avatar || initials(p.name)}</span><div><b>{p.name}</b><span>{p.name.toLowerCase().replace(' ', '.')}@club.com</span></div></div></td><td><span className="team-cell"><i style={{
                  background: teamColor(p.team, teams)
                }} />{p.team}</span></td><td><span className="jersey">#{p.number}</span></td><td>{p.position}</td><td>{p.age}</td><td><Status>{p.status}</Status></td><td><div className="row-actions"><button onClick={() => openForm('player', p)} title="Edit"><Pencil size={15} /></button><button onClick={() => remove('player', p.id)} title="Delete"><Trash2 size={15} /></button></div></td></tr>)}</tbody></table>{!filtered.length && <Empty text="Add a player or refine your search." />}<div className="table-pagination"><span>Showing <b>{filtered.length ? 1 : 0}–{filtered.length}</b> of <b>{filtered.length}</b> players</span><div className="pagination"><button disabled><ChevronLeft size={16} /></button><button className="page-current">1</button><button disabled><ChevronRight size={16} /></button></div></div></div></>;
}
