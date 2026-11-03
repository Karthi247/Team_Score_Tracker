import { useState } from 'react';
import { Field, Modal } from './ui.jsx';
export { EntityModal };
function EntityModal({
  modal,
  close,
  save,
  teams,
  tournaments
}) {
  const {
    type,
    item
  } = modal;
  const schema = {
    team: [['name', 'Team name', 'text', true], ['captain', 'Captain name', 'text', true], ['logoUrl', 'Team logo URL', 'url', false], ['status', 'Status', 'select', true, ['Active', 'Inactive']], ['description', 'Description', 'text', false]],
    player: [['name', 'Player name', 'text', true], ['team', 'Team', 'select', true, teams.map(x => x.name)], ['number', 'Jersey number', 'number', true], ['position', 'Position', 'select', true, ['Goalkeeper', 'Defender', 'Midfielder', 'Forward']], ['age', 'Age', 'number', true], ['status', 'Status', 'select', true, ['Active', 'Injured', 'Inactive']]],
    tournament: [['name', 'Tournament name', 'text', true], ['sport', 'Sport type', 'select', true, ['Football', 'Basketball', 'Cricket', 'Volleyball']], ['start', 'Start date', 'date', true], ['end', 'End date', 'date', true], ['venue', 'Venue', 'text', true], ['teams', 'Maximum teams', 'number', true], ['format', 'Tournament format', 'select', true, ['Knockout', 'Round Robin', 'League']], ['status', 'Status', 'select', true, ['Upcoming', 'In progress', 'Completed']]],
    match: [['home', 'Home team', 'select', true, teams.map(x => x.name)], ['away', 'Away team', 'select', true, teams.map(x => x.name)], ['tournament', 'Tournament', 'select', false, tournaments.map(x => x.name)], ['date', 'Match date & time', 'datetime-local', true], ['venue', 'Venue', 'text', true], ['status', 'Status', 'select', true, ['Upcoming', 'Live', 'Completed']]]
  };
  const defaults = {
    team: {
      status: 'Active',
      players: 16,
      logoUrl: ''
    },
    player: {
      status: 'Active',
      age: 22
    },
    tournament: {
      status: 'Upcoming',
      teams: 8,
      format: 'League'
    },
    match: {
      status: 'Upcoming',
      homeScore: 0,
      awayScore: 0
    }
  };
  const [values, setValues] = useState({
    ...defaults[type],
    ...item
  });
  const change = e => setValues({
    ...values,
    [e.target.name]: e.target.value
  });
  const submit = e => {
    e.preventDefault();
    if (type === 'match' && values.home === values.away) {
      window.alert('Choose two different teams.');
      return;
    }
    if (type === 'tournament' && values.end < values.start) {
      window.alert('End date must be after start date.');
      return;
    }
    save({
      ...values,
      players: Number(values.players) || 0,
      number: Number(values.number) || 0,
      age: Number(values.age) || 0,
      teams: Number(values.teams) || 0,
      homeScore: Number(values.homeScore) || 0,
      awayScore: Number(values.awayScore) || 0
    });
  };
  const titles = {
    team: 'Add a team',
    player: 'Add a player',
    tournament: 'Create a tournament',
    match: 'Schedule a match'
  };
  return <Modal title={item ? `Edit ${type}` : titles[type]} onClose={close} onSubmit={submit} submitLabel={item ? 'Save changes' : titles[type]}><div className="modal-fields">{schema[type].map(([name, label, kind, required, options]) => <Field key={name} name={name} label={label} type={kind === 'select' ? 'text' : kind} required={required} options={kind === 'select' ? options : undefined} placeholder={item?.[name] ?? undefined} value={values[name] ?? ''} onChange={change} />)}</div></Modal>;
}
