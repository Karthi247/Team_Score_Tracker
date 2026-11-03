import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { CalendarDays, Trophy, Users, Volleyball, Flag, ArrowLeft, MapPin } from 'lucide-react';
import { standings } from '../../data/mockData.js';
import { Status, Metric, TeamLogo, NotFound } from '../../components/common/ui.jsx';
import { fmtDate } from '../../utils/format.js';
import { getTournamentTeams } from '../../services/tournamentService.js';
import { fromTeam } from '../../utils/apiMappers.js';
import { LeaderboardTable } from '../leaderboard/Leaderboard.jsx';
export { TournamentDetail };
function TournamentDetail({
  tournaments,
  teams,
  matches
}) {
  const {
    id
  } = useParams();
  const tournament = tournaments.find(t => t.id === Number(id));
  const [tab, setTab] = useState('Overview');
  const [registeredTeams, setRegisteredTeams] = useState([]);
  useEffect(() => { getTournamentTeams(id).then(items => setRegisteredTeams(items.map(fromTeam))).catch(() => setRegisteredTeams([])); }, [id]);
  if (!tournament) return <NotFound label="Tournament" />;
  return <><Link to="/tournaments" className="back-link"><ArrowLeft size={15} /> Back to tournaments</Link><div className="tournament-detail-hero"><div className="tournament-detail-badge"><Trophy size={30} /></div><div className="detail-title"><div className="eyebrow">{tournament.sport.toUpperCase()} COMPETITION</div><h1>{tournament.name}</h1><p><CalendarDays size={14} />{fmtDate(tournament.start)} – {fmtDate(tournament.end)} <span>·</span><MapPin size={14} />{tournament.venue}</p></div><Status>{tournament.status}</Status></div><div className="detail-metrics"><Metric label="Registered teams" value={registeredTeams.length} icon={Users} /><Metric label="Matches played" value={matches.filter(m => m.tournament === tournament.name && m.status === 'Completed').length} icon={Volleyball} /><Metric label="Tournament format" value={tournament.format} icon={Flag} /><Metric label="Sport" value={tournament.sport} icon={Trophy} /></div><div className="tabs">{['Overview', 'Teams', 'Fixtures', 'Points table', 'Statistics'].map(t => <button className={tab === t ? 'tab-active' : ''} onClick={() => setTab(t)} key={t}>{t}</button>)}</div><div className="panel detail-tab-panel">{tab === 'Overview' ? <><h2>Tournament overview</h2><p className="muted">Track the competition from opening day to the final whistle. {tournament.name} features {tournament.maxTeams} teams competing in a {tournament.format.toLowerCase()} format.</p><div className="progress-label"><span>Competition progress</span><b>{tournament.status === 'Completed' ? '100%' : tournament.status === 'Upcoming' ? '0%' : '58%'}</b></div><div className="progress-track"><i style={{
            width: tournament.status === 'Completed' ? '100%' : tournament.status === 'Upcoming' ? '0%' : '58%'
          }} /></div></> : tab === 'Teams' ? <div className="team-list">{registeredTeams.map((team, i) => <div className="team-list-row" key={team.id}><span className={`rank rank-${i + 1}`}>{String(i + 1).padStart(2, '0')}</span><TeamLogo name={team.name} teams={teams} /><b>{team.name}</b><span className="team-record">{team.wins} wins</span></div>)}</div> : tab === 'Fixtures' ? matches.filter(m => m.tournament === tournament.name).map(m => <div className="simple-row" key={m.id}><b>{m.home} {m.status === 'Upcoming' ? 'vs' : `${m.homeScore} – ${m.awayScore}`} {m.away}</b><span>{fmtDate(m.date)}</span><Status>{m.status}</Status></div>) : <><h2>{tab}</h2><p className="muted">Standings and competition insights are updated as results are recorded.</p><LeaderboardTable data={standings} /></>}</div></>;
}
