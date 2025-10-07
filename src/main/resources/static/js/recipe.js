document.addEventListener('DOMContentLoaded', function() {
    initializeSearchForm();
    initializeStarRating();
});

function initializeSearchForm() {
    const form = document.getElementById('searchForm');
    const inputs = form.querySelectorAll('input:not([name="rating"]), select');

    inputs.forEach(input => {
        input.addEventListener('change', () => form.submit());
    });
}

function initializeStarRating() {
    const ratingInput = document.getElementById('ratingInput');
    const stars = document.querySelectorAll('.star-rating-input .star');
    const currentRating = parseFloat(ratingInput.value) || 0;

    // Set initial state
    updateStars(stars, currentRating);

    // Add event listeners to stars
    stars.forEach(star => {
        star.addEventListener('click', function() {
            const rating = this.dataset.rating;
            ratingInput.value = rating;
            updateStars(stars, rating);
            document.getElementById('searchForm').submit();
        });

        star.addEventListener('mouseover', function() {
            const rating = this.dataset.rating;
            highlightStars(stars, rating);
        });
    });

    // Reset stars on mouseout
    document.querySelector('.star-rating-input').addEventListener('mouseout', function() {
        updateStars(stars, ratingInput.value);
    });
}

function updateStars(stars, rating) {
    stars.forEach(star => {
        star.classList.toggle('active', star.dataset.rating <= rating);
    });
}

function highlightStars(stars, rating) {
    stars.forEach(star => {
        star.classList.toggle('active', star.dataset.rating <= rating);
    });
}

function showRecipe(id) {
    fetch(`/recipes/${id}`)
        .then(response => {
            if (!response.ok) throw new Error('Recipe not found');
            return response.json();
        })
        .then(recipe => {
            updateDrawerContent(recipe);
            openDrawer();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading recipe details');
        });
}

function updateDrawerContent(recipe) {
    document.getElementById('drawerTitle').textContent = recipe.title;
    document.getElementById('drawerCuisine').textContent = recipe.cuisine;
    document.getElementById('drawerDescription').textContent = recipe.description || 'No description available';
    document.getElementById('drawerTotalTime').textContent = `${recipe.total_time || 0} mins`;
    document.getElementById('drawerPrepTime').textContent = `${recipe.prep_time || 0} mins`;
    document.getElementById('drawerCookTime').textContent = `${recipe.cook_time || 0} mins`;

    updateNutritionTable(recipe.nutrients);
}

function updateNutritionTable(nutrients) {
    const nutrientsTable = document.getElementById('nutrientsTable');
    nutrientsTable.innerHTML = '';

    if (nutrients) {
        const keys = [
            'calories',
            'carbohydrateContent',
            'cholesterolContent',
            'fiberContent',
            'proteinContent',
            'saturatedFatContent',
            'sodiumContent',
            'sugarContent',
            'fatContent'
        ];

        keys.forEach(key => {
            if (nutrients[key]) {
                nutrientsTable.innerHTML += `
                    <tr>
                        <td>${formatNutrientName(key)}</td>
                        <td>${nutrients[key]}</td>
                    </tr>
                `;
            }
        });

        if (nutrientsTable.innerHTML === '') {
            nutrientsTable.innerHTML = '<tr><td colspan="2">No nutrition information available</td></tr>';
        }
    }
}

function formatNutrientName(key) {
    return key
        .replace(/([A-Z])/g, ' $1')
        .toLowerCase()
        .replace(/^./, str => str.toUpperCase());
}

function toggleTimeDetails() {
    const details = document.getElementById('timeDetails');
    const icon = document.getElementById('timeToggleIcon');

    if (details.style.display === 'none') {
        details.style.display = 'block';
        icon.className = 'fas fa-chevron-up';
    } else {
        details.style.display = 'none';
        icon.className = 'fas fa-chevron-down';
    }
}

function openDrawer() {
    document.getElementById('recipeDrawer').classList.add('open');
}

function closeDrawer() {
    document.getElementById('recipeDrawer').classList.remove('open');
}

// Close drawer when clicking outside
document.addEventListener('click', (e) => {
    const drawer = document.getElementById('recipeDrawer');
    if (drawer && !drawer.contains(e.target) && !e.target.closest('.recipe-row')) {
        closeDrawer();
    }
});