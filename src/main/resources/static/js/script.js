// script.js

/**
 * Открыть модальное окно для редактирования содержимого поля
 * @param {string} textareaId ID textarea на основной форме
 * @param {string} title Заголовок модального окна (необязательно)
 */
function openModal(textareaId, title) {
    const original = document.getElementById(textareaId);
    const modal = document.getElementById('myModal');
    const modalTextarea = document.getElementById('modalTextarea');
    const modalTitle = document.getElementById('modalTitle');

    // Подставляем текст из оригинального поля
    modalTextarea.value = original.value;

    // Устанавливаем заголовок, если нужно
    if (title) {
        modalTitle.textContent = title;
    }

    // Показываем модальное окно
    modal.style.display = 'block';
}

/** Закрыть модальное окно */
function closeModal() {
    const modal = document.getElementById('myModal');
    modal.style.display = 'none';
}

/**
 * Сохранить введённый в модальное окно текст обратно в оригинальный textarea
 * @param {string} textareaId ID textarea на основной форме
 */
function saveModal(textareaId) {
    const modalTextarea = document.getElementById('modalTextarea');
    const original = document.getElementById(textareaId);

    // Возвращаем отредактированный текст
    original.value = modalTextarea.value;

    // Закрываем модалку
    closeModal();
}

// Закрыть окно при клике вне модального контента
window.onclick = function(event) {
    const modal = document.getElementById('myModal');
    if (event.target === modal) {
        closeModal();
    }
};
