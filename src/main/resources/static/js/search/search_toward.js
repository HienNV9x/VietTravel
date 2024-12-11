    document.getElementById('search-input').addEventListener('keydown', function(event) {
      if (event.key === 'Enter') {
        handleSearch();
      }
    });

    function handleSearch() {
      const keywords = ["ha giang", "ha noi", "ha long", "ninh binh", "da nang", "hoi an", "da lat", "ho chi minh city", "phu quoc", "ca mau"];
      const searchInput = document.getElementById('search-input').value.toLowerCase();

      if (keywords.includes(searchInput)) {		        	
    	localStorage.setItem('lastSearch', searchInput);				// Save to LocalStorage
        window.open('search_room', '_blank');
      } else {
        showWarningToast_3();
      }
      document.getElementById('search-input').value = '';
    }