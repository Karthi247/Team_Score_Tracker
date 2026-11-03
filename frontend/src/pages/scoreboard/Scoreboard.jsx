import { useEffect, useState } from 'react';
import { CalendarDays, MoreHorizontal, ShieldCheck, Trophy, Zap, CirclePlay, CircleStop, ArrowLeft, MapPin } from 'lucide-react';
import { initialTeams } from '../../data/mockData.js';
import { PageTitle, TeamLogo } from '../../components/common/ui.jsx';
import { addScoreEvent, completeMatch, getMatchDetails } from '../../services/matchService.js';
import { apiErrorMessage } from '../../services/api.js';
import { fromMatch } from '../../utils/apiMappers.js';
export { Scoreboard };
function Scoreboard({
  matches,
  setMatches,
  notify,
  apiConnected
}) {
  const match = matches.find(x => x.status === 'Live') || matches[0];
  const [scores, setScores] = useState([match?.homeScore || 0, match?.awayScore || 0]);
  const [seconds, setSeconds] = useState(18 * 60 + 42);
  const [running, setRunning] = useState(true);
  const [events, setEvents] = useState([{
    id: 1,
    minute: 62,
    text: `${match?.home || 'Harbor Hawks'} scored`,
    kind: 'goal'
  }, {
    id: 2,
    minute: 48,
    text: 'Second half started',
    kind: 'info'
  }, {
    id: 3,
    minute: 31,
    text: `${match?.away || 'City Strikers'} scored`,
    kind: 'goal'
  }]);
  const [modal, setModal] = useState(false);
  useEffect(() => {
    setScores([match?.homeScore || 0, match?.awayScore || 0]);
    if (!match?.id || !apiConnected) return;
    getMatchDetails(match.id).then(details => {
      setScores([details.teamAScore || 0, details.teamBScore || 0]);
      setEvents((details.scoreEvents || []).map(event => ({ id: event.id, minute: event.minuteOrPeriod || '', text: event.description || `${event.teamName} scored`, kind: event.points < 0 ? 'info' : 'goal' })));
    }).catch(error => notify(apiErrorMessage(error)));
  }, [match?.id, apiConnected]);
  useEffect(() => {
    if (!running) return;
    const i = setInterval(() => setSeconds(s => s + 1), 1000);
    return () => clearInterval(i);
  }, [running]);
  const change = async (side, delta) => {
    if (apiConnected && match?.id) {
      try {
        const updated = await addScoreEvent(match.id, { teamId: side === 0 ? match.homeId : match.awayId, points: delta, eventType: delta < 0 ? 'SUBTRACT' : 'POINT', minuteOrPeriod: `${Math.floor(seconds / 60)}'` });
        const normalized = fromMatch(updated);
        setScores([normalized.homeScore, normalized.awayScore]);
        setMatches(current => current.map(item => item.id === match.id ? { ...item, ...normalized } : item));
        setEvents(current => [{ id: Date.now(), minute: Math.floor(seconds / 60), text: `${side === 0 ? match.home : match.away} ${delta > 0 ? 'scored' : 'score adjusted'}`, kind: delta > 0 ? 'goal' : 'info' }, ...current]);
        notify('Score saved to the server.');
      } catch (error) { notify(apiErrorMessage(error)); }
      return;
    }
    const next = [...scores];
    next[side] = Math.max(0, next[side] + delta);
    setScores(next);
    if (delta > 0) {
      const event = {
        id: Date.now(),
        minute: Math.floor(seconds / 60),
        text: `${side === 0 ? match.home : match.away} scored`,
        kind: 'goal'
      };
      setEvents([event, ...events]);
      notify('Score updated locally');
    }
  };
  const endMatch = async () => {
    if (apiConnected && match?.id) {
      try {
        const result = await completeMatch(match.id);
        const normalized = fromMatch(result);
        setMatches(current => current.map(item => item.id === match.id ? { ...item, ...normalized } : item));
        setModal(false);
        setRunning(false);
        notify('Match completed on the server.');
      } catch (error) { notify(apiErrorMessage(error)); }
      return;
    }
    setRunning(false);
    setMatches(ms => ms.map(m => m.id === match.id ? {
      ...m,
      homeScore: scores[0],
      awayScore: scores[1],
      status: 'Completed'
    } : m));
    setModal(false);
    notify(`Match finished — ${scores[0] === scores[1] ? 'Draw' : `${scores[0] > scores[1] ? match.home : match.away} win`}`);
  };
  const mins = String(Math.floor(seconds / 60)).padStart(2, '0'),
    secs = String(seconds % 60).padStart(2, '0');
  return <><PageTitle eyebrow="MATCH CENTER" title="Live scoreboard" description="Keep the match moving. Every moment, right here." action={<span className="local-mode"><i /> Local simulation</span>} /><div className="scoreboard-shell"><div className="scoreboard-top"><div><span className="live-indicator"><i /> LIVE MATCH</span><h2>{match?.tournament || 'Premier League 2025'}</h2><p>Matchday 14 <i /> Central Sports Arena</p></div><button className="scoreboard-menu icon-btn" onClick={() => notify('Match settings are ready to configure')}><MoreHorizontal size={19} /></button></div><div className="scoreboard-main"><div className="score-team"><TeamLogo name={match?.home || 'Harbor Hawks'} teams={initialTeams} size="score" /><h3>{match?.home || 'Harbor Hawks'}</h3><span>HOME</span><div className="score-controls"><button onClick={() => change(0, -1)} aria-label="Subtract home score">−</button><strong>{scores[0]}</strong><button onClick={() => change(0, 1)} aria-label="Add home score">+</button></div></div><div className="score-center"><span className="score-period">SECOND HALF</span><div className="score-clock">{mins}<i>:</i>{secs}</div><div className="clock-controls"><button onClick={() => setRunning(!running)}>{running ? <><CircleStop size={15} /> Pause clock</> : <><CirclePlay size={15} /> Resume clock</>}</button><button onClick={() => setSeconds(0)}><ArrowLeft size={14} /> Reset</button></div><div className="score-separator"><span /> FULL TIME AT 90’ <span /></div></div><div className="score-team"><TeamLogo name={match?.away || 'City Strikers'} teams={initialTeams} size="score" /><h3>{match?.away || 'City Strikers'}</h3><span>AWAY</span><div className="score-controls"><button onClick={() => change(1, -1)} aria-label="Subtract away score">−</button><strong>{scores[1]}</strong><button onClick={() => change(1, 1)} aria-label="Add away score">+</button></div></div></div><div className="scoreboard-actions"><span><i /> Scores update instantly on this device</span><button className="btn btn-danger-soft" onClick={() => setModal(true)}><CircleStop size={16} /> End match</button></div></div><div className="score-lower"><section className="panel"><div className="panel-heading"><div><h2>Match events</h2><p>Key moments from the pitch</p></div><button className="icon-btn" onClick={() => notify('Event log is up to date')}><MoreHorizontal size={18} /></button></div><div className="event-list">{events.map(e => <div className="event-row" key={e.id}><div className={`event-icon ${e.kind}`}><Zap size={15} /></div><div><b>{e.text}</b><span>{e.kind === 'goal' ? 'Goal' : 'Match update'}</span></div><small>{e.minute}’</small></div>)}</div></section><section className="panel"><div className="panel-heading"><div><h2>Match details</h2><p>Fixture information</p></div></div><div className="match-details"><div><CalendarDays size={16} /><span><small>DATE & TIME</small><b>June 15, 2025 · 4:30 PM</b></span></div><div><MapPin size={16} /><span><small>VENUE</small><b>Central Sports Arena</b></span></div><div><Trophy size={16} /><span><small>COMPETITION</small><b>{match?.tournament || 'Premier League 2025'}</b></span></div></div><div className="temp-note"><ShieldCheck size={15} /><span><b>Development preview</b> Score changes are temporary and stored in local page state. Connect a backend service to persist live match data.</span></div></section></div>{modal && <div className="modal-backdrop"><div className="confirm-card"><span className="confirm-icon"><CircleStop size={22} /></span><h2>End this match?</h2><p>This will mark the fixture as completed with the current score of <b>{scores[0]} – {scores[1]}</b>.</p><div className="modal-foot"><button className="btn btn-light" onClick={() => setModal(false)}>Keep playing</button><button className="btn btn-primary" onClick={endMatch}>End match</button></div></div></div>}</>;
}
