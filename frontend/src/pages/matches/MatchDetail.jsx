import { Link, useParams } from 'react-router-dom';
import { CalendarDays, Trophy, Pencil, ArrowLeft, MapPin } from 'lucide-react';
import { PageTitle, Status, TeamLogo, NotFound } from '../../components/common/ui.jsx';
import { fmtDate } from '../../utils/format.js';
export { MatchDetail };
function MatchDetail({
  matches,
  teams,
  openForm
}) {
  const {
    id
  } = useParams();
  const match = matches.find(m => m.id === Number(id));
  if (!match) return <NotFound label="Match" />;
  return <><Link to="/matches" className="back-link"><ArrowLeft size={15} /> Back to matches</Link><PageTitle eyebrow="FIXTURE DETAILS" title="Match details" description={`${match.tournament} · ${fmtDate(match.date)}`} action={<button className="btn btn-light" onClick={() => openForm('match', match)}><Pencil size={15} /> Edit fixture</button>} /><div className="panel match-detail-panel"><div className="match-detail-competition"><Trophy size={15} />{match.tournament}<Status>{match.status}</Status></div><div className="match-detail-score"><div><TeamLogo name={match.home} teams={teams} size="large" /><h2>{match.home}</h2><span>HOME</span></div><div className="match-detail-center"><b>{match.status === 'Upcoming' ? 'VS' : `${match.homeScore} – ${match.awayScore}`}</b><span>{match.status === 'Live' ? 'LIVE' : fmtDate(match.date)}</span></div><div><TeamLogo name={match.away} teams={teams} size="large" /><h2>{match.away}</h2><span>AWAY</span></div></div><div className="match-detail-information"><div><CalendarDays size={17} /><span><small>DATE & TIME</small><b>{new Date(match.date).toLocaleString('en-US', {
                weekday: 'long',
                month: 'long',
                day: 'numeric',
                year: 'numeric',
                hour: 'numeric',
                minute: '2-digit'
              })}</b></span></div><div><MapPin size={17} /><span><small>VENUE</small><b>{match.venue}</b></span></div><div><Trophy size={17} /><span><small>COMPETITION</small><b>{match.tournament}</b></span></div></div></div></>;
}
