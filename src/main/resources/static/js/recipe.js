document.addEventListener('DOMContentLoaded', function() {
    // Initialize form handlers
    initializeSearchForm();
});

function initializeSearchForm() {
    const form = document.getElementById('searchForm');
    const inputs = form.querySelectorAll('input, select');

    inputs.forEach(input => {
        input.addEventListener('change', () => {
            form.submit();
        });
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
    document.getElementById('drawerDescription').textContent = recipe.description;
    document.getElementById('drawerTotalTime').textContent = `${recipe.total_time} mins`;
    document.getElementById('drawerPrepTime').textContent = `${recipe.prep_time} mins`;
    document.getElementById('drawerCookTime').textContent = `${recipe.cook_time} mins`;

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
                const row = `
                    <tr>
                        <td>${formatNutrientName(key)}</td>
                        <td>${nutrients[key]}</td>
                    </tr>
                `;
                nutrientsTable.innerHTML += row;
            }
        });
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