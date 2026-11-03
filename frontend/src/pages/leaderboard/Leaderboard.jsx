import { useEffect, useState } from 'react';
import { ChevronLeft, ChevronRight, Search, Trophy } from 'lucide-react';
import { initialTeams } from '../../data/mockData.js';
import { PageTitle, TeamLogo, Empty } from '../../components/common/ui.jsx';
import { getStandings } from '../../services/standingService.js';
import { fromStanding } from '../../utils/apiMappers.js';

export function Leaderboard({ tournaments, notify }) {
  const [search, setSearch] = useState('');
  const [selectedId, setSelectedId] = useState(tournaments[0]?.id || '');
  const [rows, setRows] = useState([]);

  useEffect(() => {
    if (!selectedId) return;
    getStandings(selectedId)
      .then(items => setRows(items.map(fromStanding)))
      .catch(error => { setRows([]); notify?.(error.response?.data?.message || 'Could not load this tournament’s standings.'); });
  }, [selectedId, notify]);

  const visible = rows.filter(team => team.name.toLowerCase().includes(search.toLowerCase()));
  return <>
    <PageTitle eyebrow="COMPETITION STATS" title="Leaderboard" description="The race to the top. Standings across your competitions." action={<select className="filter-select" value={selectedId} onChange={event => setSelectedId(event.target.value)}><option value="">Choose a tournament</option>{tournaments.map(tournament => <option key={tournament.id} value={tournament.id}>{tournament.name}</option>)}</select>} />
    {visible.length > 0 && <div className="leaderboard-podium">{visible.slice(0, 3).map((team, index) => <div className={`podium-card podium-${index + 1}`} key={team.id}><span className="podium-rank">{index === 0 ? <Trophy size={18} /> : `0${index + 1}`}</span><TeamLogo name={team.name} teams={initialTeams} /><b>{team.name}</b><span>{team.wins} wins · {team.draws} draws</span><strong>{team.points} <small>PTS</small></strong></div>)}</div>}
    <div className="panel table-panel leaderboard-table-panel"><div className="panel-heading"><div><h2>League standings</h2><p>Updated after every match</p></div><label className="search-box compact-search"><Search size={16} /><input value={search} onChange={event => setSearch(event.target.value)} placeholder="Find a team..." /></label></div>{visible.length ? <LeaderboardTable data={visible} /> : <Empty text="Select a tournament with registered teams to see the standings." />}<div className="table-pagination"><span>Showing <b>{visible.length}</b> of <b>{rows.length}</b> teams</span><div className="pagination"><button disabled><ChevronLeft size={16} /></button><button className="page-current">1</button><button disabled><ChevronRight size={16} /></button></div></div></div>
  </>;
}

export function LeaderboardTable({ data }) {
  return <table className="data-table leaderboard-table"><thead><tr><th>POS</th><th>TEAM</th><th>MP</th><th>W</th><th>D</th><th>L</th><th>PF</th><th>PA</th><th>DIFF</th><th>PTS</th></tr></thead><tbody>{data.map((team, index) => <tr key={team.id}><td><span className={`rank rank-${index + 1}`}>{String(index + 1).padStart(2, '0')}</span></td><td><div className="leader-team-cell"><TeamLogo name={team.name} teams={initialTeams} /><b>{team.name}</b></div></td><td>{team.played}</td><td>{team.wins}</td><td>{team.draws}</td><td>{team.losses}</td><td>{team.scored}</td><td>{team.conceded}</td><td className={team.scored - team.conceded >= 0 ? 'positive' : 'negative'}>{team.scored - team.conceded > 0 ? '+' : ''}{team.scored - team.conceded}</td><td><b className="points-pill">{team.points}</b></td></tr>)}</tbody></table>;
}
