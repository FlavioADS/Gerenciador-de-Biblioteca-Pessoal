document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('img[data-fallback]').forEach((img) => {
    const fallback = img.dataset.fallback;
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
