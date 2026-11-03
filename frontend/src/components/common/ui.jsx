import { Link } from 'react-router-dom';
import { Check, ChevronRight, X, ClipboardList } from 'lucide-react';
import { initials } from '../../utils/format.js';
export { Status, PageTitle, Modal, Field, Toast, ChartTooltip, Empty, teamColor, TeamLogo, QuickAction, Metric, NotFound };
function Status({
  children
}) {
  const val = (children || '').toLowerCase().replace(' ', '-');
  return <span className={`status status-${val}`}><i />{children}</span>;
}
function PageTitle({
  eyebrow,
  title,
  description,
  action
}) {
  return <div className="page-title"><div><div className="eyebrow">{eyebrow}</div><h1>{title}</h1><p>{description}</p></div>{action}</div>;
}
function Modal({
  title,
  children,
  onClose,
  onSubmit,
  submitLabel = 'Save changes'
}) {
  return <div className="modal-backdrop" onMouseDown={onClose}><div className="modal-card" onMouseDown={e => e.stopPropagation()}><div className="modal-head"><div><h2>{title}</h2><p>Fill in the details below to continue.</p></div><button className="icon-btn" onClick={onClose}><X size={19} /></button></div><form onSubmit={onSubmit}><div className="modal-body">{children}</div><div className="modal-foot"><button type="button" className="btn btn-light" onClick={onClose}>Cancel</button><button className="btn btn-primary">{submitLabel}</button></div></form></div></div>;
}
function Field({
  label,
  name,
  value,
  onChange,
  required,
  type = 'text',
  options,
  placeholder
}) {
  const valueProps = onChange ? {
    value: value ?? ''
  } : {
    defaultValue: value ?? ''
  };
  return <label className="field"><span>{label}{required && <b> *</b>}</span>{options ? <select name={name} {...valueProps} onChange={onChange} required={required}><option value="">Select {label.toLowerCase()}</option>{options.map(x => <option key={x} value={x}>{x}</option>)}</select> : <input name={name} {...valueProps} onChange={onChange} required={required} type={type} placeholder={placeholder || `Enter ${label.toLowerCase()}`} />}</label>;
}
function Toast({
  text
}) {
  return text ? <div className="toast-pop"><Check size={17} />{text}</div> : null;
}
function ChartTooltip({
  active,
  payload,
  label
}) {
  if (!active || !payload?.length) return null;
  return <div className="chart-tooltip"><b>{label}</b><span>{payload[0].name}: {payload[0].value}</span></div>;
}
function Empty({
  text
}) {
  return <div className="empty-state"><span><ClipboardList size={20} /></span><b>Nothing to show yet</b><p>{text}</p></div>;
}
function teamColor(name, teams) {
  return teams.find(t => t.name === name)?.color || '#52637d';
}
function TeamLogo({
  name,
  teams,
  size = 'normal'
}) {
  const color = teamColor(name, teams);
  return <span className={`team-logo ${size}`} style={{
    '--team': color
  }}>{initials(name)}</span>;
}
function QuickAction({
  icon: Icon,
  label,
  onClick
}) {
  return <button className="quick-action" onClick={onClick}><span><Icon size={18} /></span><b>{label}</b><ChevronRight size={15} /></button>;
}
function Metric({
  label,
  value,
  icon: Icon
}) {
  return <div className="metric-card"><span><Icon size={17} />{label}</span><b>{value}</b></div>;
}
function NotFound({
  label
}) {
  return <div className="not-found"><span>404</span><h2>{label} not found</h2><p>It may have been removed or the link is no longer valid.</p><Link className="btn btn-primary" to="/">Return to overview</Link></div>;
}
