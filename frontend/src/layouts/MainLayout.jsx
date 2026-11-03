import { useEffect, useState } from 'react';
import { NavLink, Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import { Activity, Bell, ChevronDown, ChevronLeft, ChevronRight, CircleHelp, LogOut, Menu, MoreHorizontal, Search, Settings } from 'lucide-react';
import { initialMatches, initialPlayers, initialTeams, initialTournaments, standings } from '../data/mockData.js';
import { loadCollection, saveCollection } from '../services/mockService.js';
import { navGroups } from '../utils/navigation.js';
import { AuthPage } from '../pages/auth/AuthPage.jsx';
import { Dashboard } from '../components/dashboard/Dashboard.jsx';
import { TeamsPage } from '../pages/teams/TeamsPage.jsx';
import { TeamDetail } from '../pages/teams/TeamDetail.jsx';
import { PlayersPage } from '../pages/players/PlayersPage.jsx';
import { TournamentsPage } from '../pages/tournaments/TournamentsPage.jsx';
import { TournamentDetail } from '../pages/tournaments/TournamentDetail.jsx';
import { MatchesPage } from '../pages/matches/MatchesPage.jsx';
import { MatchDetail } from '../pages/matches/MatchDetail.jsx';
import { Scoreboard } from '../pages/scoreboard/Scoreboard.jsx';
import { Leaderboard } from '../pages/leaderboard/Leaderboard.jsx';
import { AnalyticsPage } from '../pages/analytics/AnalyticsPage.jsx';
import { SettingsPage } from '../pages/settings/SettingsPage.jsx';
import { EntityModal } from '../components/common/EntityModal.jsx';
import { Toast } from '../components/common/ui.jsx';
import { apiErrorMessage } from '../services/api.js';
import { login, signup, getCurrentUser, logout } from '../services/authService.js';
import { getTeams, createTeam, updateTeam, deleteTeam } from '../services/teamService.js';
import { getPlayers, createPlayer, updatePlayer, deletePlayer } from '../services/playerService.js';
import { getTournaments, createTournament, updateTournament, deleteTournament } from '../services/tournamentService.js';
import { getMatches, createMatch, updateMatch, deleteMatch } from '../services/matchService.js';
import { getDashboard } from '../services/dashboardService.js';
import { fromTeam, toTeamRequest, fromPlayer, toPlayerRequest, fromTournament, toTournamentRequest, fromMatch, toMatchRequest } from '../utils/apiMappers.js';
export default App;
function App() {
  const [teams, setTeams] = useState(() => loadCollection('teams', initialTeams));
  const [players, setPlayers] = useState(() => loadCollection('players', initialPlayers));
  const [tournaments, setTournaments] = useState(() => loadCollection('tournaments', initialTournaments));
  const [matches, setMatches] = useState(() => loadCollection('matches', initialMatches));
  const [toast, setToast] = useState('');
  const [user, setUser] = useState(() => localStorage.getItem('tracker:user') || 'Alex Morgan');
  const [modal, setModal] = useState(null);
  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const [apiState, setApiState] = useState('connecting');
  const [dashboardData, setDashboardData] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  useEffect(() => saveCollection('teams', teams), [teams]);
  useEffect(() => saveCollection('players', players), [players]);
  useEffect(() => saveCollection('tournaments', tournaments), [tournaments]);
  useEffect(() => saveCollection('matches', matches), [matches]);
  useEffect(() => {
    let active = true;
    Promise.allSettled([getTeams(), getPlayers(), getTournaments(), getMatches(), getDashboard()]).then(results => {
      if (!active) return;
      const [teamResult, playerResult, tournamentResult, matchResult, dashboardResult] = results;
      if (teamResult.status === 'fulfilled') setTeams(teamResult.value.map(fromTeam));
      if (playerResult.status === 'fulfilled') setPlayers(playerResult.value.map(fromPlayer));
      if (tournamentResult.status === 'fulfilled') setTournaments(tournamentResult.value.map(fromTournament));
      if (matchResult.status === 'fulfilled') setMatches(matchResult.value.map(fromMatch));
      if (dashboardResult.status === 'fulfilled') setDashboardData(dashboardResult.value);
      const connected = results.slice(0, 4).every(result => result.status === 'fulfilled');
      setApiState(connected ? 'connected' : 'offline');
      if (!connected) setToast('Backend connection unavailable; showing local demo data where needed.');
    });
    const token = localStorage.getItem('tracker:token');
    if (token) getCurrentUser().then(profile => { if (active) setUser(profile.fullName); }).catch(() => { logout(); });
    return () => { active = false; };
  }, []);
  useEffect(() => {
    if (!toast) return;
    const t = setTimeout(() => setToast(''), 2800);
    return () => clearTimeout(t);
  }, [toast]);
  const routeNames = {
    '/': 'Overview',
    '/teams': 'Teams',
    '/players': 'Players',
    '/tournaments': 'Tournaments',
    '/matches': 'Matches',
    '/scoreboard': 'Live scoreboard',
    '/leaderboard': 'Leaderboard',
    '/analytics': 'Analytics',
    '/settings': 'Settings'
  };
  const notify = message => setToast(message);
  const mutation = async (type, item, id) => {
    try {
      let saved;
      if (type === 'team') saved = fromTeam(id ? await updateTeam(id, toTeamRequest(item)) : await createTeam(toTeamRequest(item)));
      if (type === 'player') saved = fromPlayer(id ? await updatePlayer(id, toPlayerRequest(item, teams)) : await createPlayer(toPlayerRequest(item, teams)));
      if (type === 'tournament') saved = fromTournament(id ? await updateTournament(id, toTournamentRequest(item)) : await createTournament(toTournamentRequest(item)));
      if (type === 'match') saved = fromMatch(id ? await updateMatch(id, toMatchRequest(item, teams, tournaments)) : await createMatch(toMatchRequest(item, teams, tournaments)));
      const setters = { team: setTeams, player: setPlayers, tournament: setTournaments, match: setMatches };
      setters[type](list => id ? list.map(row => row.id === id ? saved : row) : [saved, ...list]);
      setModal(null);
      notify(`${type[0].toUpperCase() + type.slice(1)} ${id ? 'updated' : 'created'} on the server.`);
    } catch (error) {
      setApiState(error.response ? 'connected' : 'offline');
      notify(apiErrorMessage(error));
    }
  };
  const remove = async (type, id) => {
    if (!window.confirm(`Delete this ${type}? This action cannot be undone.`)) return;
    try {
      const actions = { team: deleteTeam, player: deletePlayer, tournament: deleteTournament, match: deleteMatch };
      await actions[type](id);
      const setters = { team: setTeams, player: setPlayers, tournament: setTournaments, match: setMatches };
      setters[type](list => list.filter(row => row.id !== id));
      notify(`${type[0].toUpperCase() + type.slice(1)} deleted from the server.`);
    } catch (error) { notify(apiErrorMessage(error)); }
  };
  const openForm = (type, item = null) => setModal({
    type,
    item
  });
  const current = routeNames[location.pathname] || (location.pathname.startsWith('/teams/') ? 'Team details' : location.pathname.startsWith('/tournaments/') ? 'Tournament details' : location.pathname.startsWith('/matches/') ? 'Match details' : 'Overview');
  const entityProps = {
    teams,
    players,
    tournaments,
    matches,
    standings,
    openForm,
    remove,
    notify,
    dashboardData
  };
  return <Routes>
    <Route path="/login" element={<AuthPage mode="login" onLogin={auth => {
      setUser(auth.fullName || 'Alex Morgan');
      navigate('/');
      notify('Welcome back!');
    }} />} />
    <Route path="/signup" element={<AuthPage mode="signup" onLogin={auth => {
      setUser(auth.fullName || 'Alex Morgan');
      navigate('/');
      notify('Your account is ready');
    }} />} />
    <Route path="*" element={<div className={`app-shell ${collapsed ? 'is-collapsed' : ''}`}>
      <aside className={`sidebar ${mobileOpen ? 'mobile-open' : ''}`}><div className="brand"><div className="brand-mark"><Activity size={20} /></div><span>touchline<span className="brand-dot">.</span></span><button className="sidebar-collapse" onClick={() => setCollapsed(!collapsed)} title="Collapse sidebar"><ChevronLeft size={17} /></button></div>
        <div className="workspace-switch"><div className="workspace-icon">AC</div><div className="workspace-copy"><b>ACME CLUB</b><span>Pro workspace</span></div><ChevronDown size={15} /></div>
        {navGroups.map(group => <div className="nav-group" key={group.label}><div className="nav-heading">{group.label}</div>{group.items.map(([label, path, Icon]) => <NavLink onClick={() => setMobileOpen(false)} key={label} to={path} className={({
            isActive
          }) => `nav-item ${isActive ? 'active' : ''}`}><Icon size={18} /><span>{label}</span>{label === 'Live scoreboard' && <i className="live-nav-dot" />}</NavLink>)}</div>)}
        <div className="sidebar-bottom"><NavLink to="/settings" className={({
            isActive
          }) => `nav-item ${isActive ? 'active' : ''}`}><Settings size={18} /><span>Settings</span></NavLink><div className="sidebar-help"><CircleHelp size={17} /><div><b>Need a hand?</b><span>Visit our help center</span></div><ChevronRight size={15} /></div><div className="sidebar-user"><div className="avatar avatar-user">AM</div><div><b>{user}</b><span>Club administrator</span></div><MoreHorizontal size={19} /></div></div>
      </aside>
      {mobileOpen && <div className="mobile-shade" onClick={() => setMobileOpen(false)} />}
      <main className="main-area"><header className="topbar"><div className="topbar-left"><button className="mobile-menu icon-btn" onClick={() => setMobileOpen(true)}><Menu size={20} /></button><div className="breadcrumbs"><span>Workspace</span><ChevronRight size={14} /><b>{current}</b></div></div><div className="topbar-right"><span className={`api-status api-${apiState}`} title={apiState === 'connected' ? 'Connected to Spring Boot API' : apiState === 'connecting' ? 'Checking API connection' : 'Backend unavailable; demo data is shown'}><i />{apiState === 'connected' ? 'API connected' : apiState === 'connecting' ? 'Connecting' : 'Demo mode'}</span><div className="season-pill"><span className="season-dot" /> 2025 Season <ChevronDown size={14} /></div><button className="icon-btn search-shortcut" onClick={() => document.querySelector('.search-input')?.focus()}><Search size={18} /></button><button className="icon-btn notification-btn" onClick={() => notify('You’re all caught up')}><Bell size={18} /><i /></button><div className="profile-wrap"><button className="profile-button" onClick={() => setMenuOpen(!menuOpen)}><div className="avatar avatar-user">AM</div><span>{user}</span><ChevronDown size={15} /></button>{menuOpen && <div className="profile-menu"><button onClick={() => {
                  setMenuOpen(false);
                  navigate('/settings');
                }}><Settings size={15} /> Account settings</button><button onClick={() => {
                  setMenuOpen(false);
                  logout();
                  navigate('/login');
                }}><LogOut size={15} /> Sign out</button></div>}</div></div></header>
        <div className="content"><Routes><Route path="/" element={<Dashboard {...entityProps} />} /><Route path="/teams" element={<TeamsPage {...entityProps} />} /><Route path="/teams/:id" element={<TeamDetail {...entityProps} />} /><Route path="/players" element={<PlayersPage {...entityProps} />} /><Route path="/tournaments" element={<TournamentsPage {...entityProps} />} /><Route path="/tournaments/:id" element={<TournamentDetail {...entityProps} />} /><Route path="/matches" element={<MatchesPage {...entityProps} />} /><Route path="/matches/:id" element={<MatchDetail {...entityProps} />} /><Route path="/scoreboard" element={<Scoreboard matches={matches} setMatches={setMatches} notify={notify} apiConnected={apiState === 'connected'} />} /><Route path="/leaderboard" element={<Leaderboard tournaments={tournaments} notify={notify} />} /><Route path="/analytics" element={<AnalyticsPage dashboardData={dashboardData} />} /><Route path="/settings" element={<SettingsPage user={user} setUser={setUser} notify={notify} />} /><Route path="*" element={<Dashboard {...entityProps} />} /></Routes></div>
        <footer className="footer"><span>© 2025 Touchline Sports Management</span><span>Made for the love of the game <span className="footer-heart">♥</span></span></footer>
      </main>{modal && <EntityModal modal={modal} close={() => setModal(null)} save={value => mutation(modal.type, value, modal.item?.id)} teams={teams} tournaments={tournaments} />}<Toast text={toast} />
    </div>} />
  </Routes>;
}
