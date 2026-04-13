document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('img[data-fallback]').forEach((img) => {
    const fallback = img.getAttribute('data-fallback');
    if (!fallback) {
      return;
    }

    img.addEventListener('error', () => {
      if (img.src !== fallback) {
        img.src = fallback;
      }
    });
  });
});
