export const initials = (name = '') => name.split(' ').map(x => x[0]).join('').slice(0, 2).toUpperCase();
export const dateLabel = date => new Date(date).toLocaleDateString('en-US', {
  month: 'short',
  day: 'numeric'
});
export const fmtDate = date => new Date(date).toLocaleDateString('en-US', {
  month: 'short',
  day: 'numeric',
  year: 'numeric'
});
