import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Activity, ChevronRight, Trophy, Eye, EyeOff, Volleyball } from 'lucide-react';
import { Field } from '../../components/common/ui.jsx';
import { login, signup as register } from '../../services/authService.js';
import { apiErrorMessage } from '../../services/api.js';
export { AuthPage };
function AuthPage({
  mode,
  onLogin
}) {
  const signup = mode === 'signup';
  const [show, setShow] = useState(false);
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(false);
  const submit = async e => {
    e.preventDefault();
    setError('');
    const form = new FormData(e.currentTarget);
    const email = form.get('email');
    if (!/^[^@]+@[^@]+\.[^@]+$/.test(email)) {
      setError('Enter a valid email address.');
      return;
    }
    if (password.length < 8) {
      setError('Password must be at least 8 characters.');
      return;
    }
    if (signup && password !== form.get('confirm')) {
      setError('Passwords do not match.');
      return;
    }
    setLoading(true);
    try {
      const auth = signup
        ? await register({ fullName: name, email, mobileNumber: form.get('mobileNumber'), password, confirmPassword: form.get('confirm') })
        : await login({ email, password });
      onLogin(auth);
    } catch (requestError) {
      setError(apiErrorMessage(requestError));
    } finally {
      setLoading(false);
    }
  };
  const strength = password.length === 0 ? 0 : password.length < 8 ? 1 : password.length < 12 ? 2 : 3;
  return <div className="auth-layout"><div className="auth-visual"><div className="auth-brand"><div className="brand-mark"><Activity size={20} /></div><span>touchline<span className="brand-dot">.</span></span></div><div className="auth-visual-copy"><span className="auth-overline">YOUR CLUB, IN PLAY</span><h1>Every game.<br /><em>One place.</em></h1><p>The all-in-one workspace for teams that want to play, compete, and grow together.</p><div className="auth-feature"><span><Trophy size={18} /></span><div><b>Make every moment count</b><small>Live scores, clear insights, better game days.</small></div></div></div><div className="auth-decoration"><div className="field-ring ring-one" /><div className="field-ring ring-two" /><div className="field-ball"><Volleyball size={52} /></div><span className="auth-score-float">90<span>’</span></span><span className="auth-tag-float">MATCH DAY</span></div><div className="auth-visual-foot">BUILT FOR THE LOVE OF THE GAME <span>✳</span></div></div><div className="auth-main"><div className="auth-main-top"><span>Already have an account?</span><Link to={signup ? '/login' : '/signup'}>{signup ? 'Log in' : 'Create account'}</Link></div><div className="auth-form-wrap"><div className="auth-icon"><Activity size={20} /></div><div className="eyebrow">{signup ? 'GET STARTED' : 'WELCOME BACK'}</div><h2>{signup ? 'Create your account' : 'Sign in to Touchline'}</h2><p className="auth-subtitle">{signup ? 'Start bringing your club together today.' : 'Your club’s next big moment is waiting.'}</p><form onSubmit={submit} className="auth-form">{signup && <><Field label="Full name" name="fullName" required value={name} onChange={e => setName(e.target.value)} placeholder="Alex Morgan" /><Field label="Mobile number" name="mobileNumber" type="tel" required placeholder="+1 555 010 2040" /></>}<Field label="Email address" name="email" type="email" required placeholder="you@yourclub.com" /><label className="field"><span>Password</span><div className="password-input"><input name="password" type={show ? 'text' : 'password'} value={password} onChange={e => setPassword(e.target.value)} placeholder="Enter your password" required /><button type="button" onClick={() => setShow(!show)}>{show ? <EyeOff size={17} /> : <Eye size={17} />}</button></div></label>{signup && password && <div className="strength"><div className="strength-bars">{[1, 2, 3].map(x => <i className={strength >= x ? 'filled' : ''} key={x} />)}</div><span>{['', 'Too short', 'Good', 'Strong'][strength]} password</span></div>}{signup && <Field label="Confirm password" name="confirm" type={show ? 'text' : 'password'} required placeholder="Re-enter your password" />}{!signup && <div className="auth-options"><label><input type="checkbox" /> Remember me</label><button type="button" onClick={() => setError('Password reset instructions will be sent to your email.')}>Forgot password?</button></div>}{error && <div className="form-error">{error}</div>}<button disabled={loading} className="btn btn-primary auth-submit">{loading ? 'Connecting…' : signup ? 'Create account' : 'Sign in'} <ChevronRight size={17} /></button></form><div className="auth-divider"><span /> SECURE API SIGN IN <span /></div><button className="social-button" onClick={() => setError('Google sign-in is not configured.')}><span>G</span> Continue with Google</button><div className="auth-terms">By continuing, you agree to our <a href="#terms">Terms of Service</a> and <a href="#privacy">Privacy Policy</a>.</div></div><div className="auth-main-foot">© 2025 Touchline Sports Management <span>·</span> Need help? <a href="mailto:support@touchline.club">Contact support</a></div></div></div>;
}
