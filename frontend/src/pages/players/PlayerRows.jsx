export { PlayerRows };
function PlayerRows({
  players,
  teams
}) {
  return <table className="data-table"><thead><tr><th>PLAYER</th><th>JERSEY</th><th>POSITION</th><th>AGE</th></tr></thead><tbody>{players.map(p => <tr key={p.id}><td><div className="player-cell"><span className="avatar player-avatar">{p.avatar || initials(p.name)}</span><b>{p.name}</b></div></td><td>#{p.number}</td><td>{p.position}</td><td>{p.age}</td></tr>)}</tbody></table>;
}
