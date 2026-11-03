import { useEffect, useState } from 'react';
import { Activity, Bell, Settings, ShieldCheck, Users } from 'lucide-react';
import { PageTitle, Field } from '../../components/common/ui.jsx';
import { getCurrentUser, updateCurrentUser } from '../../services/authService.js';
import { apiErrorMessage } from '../../services/api.js';
export { SettingsPage };
function SettingsPage({
  user,
  setUser,
  notify
}) {
  const [tab, setTab] = useState('Profile');
  const [name, setName] = useState(user);
  const [email, setEmail] = useState('alex.morgan@acmeclub.com');
  const [mobileNumber, setMobileNumber] = useState('');
  const [saving, setSaving] = useState(false);
  const [toggles, setToggles] = useState({
    match: true,
    weekly: true,
    product: false
  });
  useEffect(() => { getCurrentUser().then(profile => { setName(profile.fullName); setEmail(profile.email); setMobileNumber(profile.mobileNumber || ''); }).catch(() => {}); }, []);
  const saveProfile = async () => {
    if (tab !== 'Profile') { notify('This preference is stored in this browser only.'); return; }
    setSaving(true);
    try {
      const profile = await updateCurrentUser({ fullName: name, mobileNumber });
      setUser(profile.fullName);
      localStorage.setItem('tracker:user', profile.fullName);
      notify('Profile saved to the server.');
    } catch (error) { notify(apiErrorMessage(error)); }
    finally { setSaving(false); }
  };
  return <><PageTitle eyebrow="PREFERENCES" title="Settings" description="Manage your profile, preferences, and club workspace." /><div className="settings-layout"><nav className="settings-nav">{[['Profile', Users], ['Account', ShieldCheck], ['Notifications', Bell], ['Appearance', Activity], ['Security', ShieldCheck]].map(([t, I]) => <button className={tab === t ? 'active' : ''} key={t} onClick={() => setTab(t)}><I size={17} />{t}</button>)}</nav><section className="panel settings-panel"><div className="panel-heading"><div><h2>{tab} settings</h2><p>Update your {tab.toLowerCase()} preferences and information.</p></div></div>{tab === 'Profile' ? <><div className="profile-edit"><div className="avatar profile-big">AM</div><div><b>Profile photo</b><span>JPG or PNG, max 2 MB</span></div><button className="btn btn-light" onClick={() => notify('Photo upload is not configured by the backend.')}>Upload photo</button></div><div className="settings-form"><Field label="Full name" value={name} onChange={e => setName(e.target.value)} required /><Field label="Email address" type="email" value={email} onChange={() => {}} required /><Field label="Mobile number" type="tel" value={mobileNumber} onChange={e => setMobileNumber(e.target.value)} required /><Field label="Role" value="Club administrator" onChange={() => {}} /></div></> : tab === 'Notifications' ? <div className="preference-list">{[['match', 'Match updates', 'Get notified about live scores and match results.'], ['weekly', 'Weekly summary', 'A weekly recap of your team and competition performance.'], ['product', 'Product news', 'Occasional updates about new features and improvements.']].map(([key, title, desc]) => <div className="preference-row" key={key}><div><b>{title}</b><span>{desc}</span></div><button className={`switch ${toggles[key] ? 'on' : ''}`} onClick={() => setToggles({
              ...toggles,
              [key]: !toggles[key]
            })}><i /></button></div>)}</div> : tab === 'Appearance' ? <div className="appearance-options"><button className="appearance-card selected"><span className="appearance-preview light-preview" /><b>Light</b><small>Clean & bright</small></button><button className="appearance-card" onClick={() => notify('Dark appearance is coming soon')}><span className="appearance-preview dark-preview" /><b>Dark</b><small>Easy on the eyes</small></button></div> : tab === 'Security' ? <div className="security-setting"><span className="security-icon"><ShieldCheck size={20} /></span><div><b>Password & security</b><p>Protect your account with a strong password and two-step verification.</p></div><button className="btn btn-light" onClick={() => notify('Security settings updated')}>Manage security</button></div> : <div className="security-setting"><span className="security-icon"><Settings size={20} /></span><div><b>Account preferences</b><p>Manage your workspace, account data, and connected devices.</p></div><button className="btn btn-light" onClick={() => notify('Account preferences saved')}>Manage account</button></div>}<div className="settings-footer"><span>Last saved just now</span><button className="btn btn-primary" onClick={() => {
            saveProfile();
          }} disabled={saving}>{saving ? 'Saving…' : 'Save changes'}</button></div></section></div></>;
}
