export const loadCollection = (key, fallback) => {
  try {
    const value = localStorage.getItem(`tracker:${key}`);
    return value ? JSON.parse(value) : fallback;
  } catch {
    return fallback;
  }
};
export const saveCollection = (key, value) => localStorage.setItem(`tracker:${key}`, JSON.stringify(value));
