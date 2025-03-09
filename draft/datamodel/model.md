# Users - пользователи
1. `id` integer [primary key]
1. `login` text [not null, unique]
1. `name` text [not null]
1. `hashed_password` text [not null]

# Sharing - права доступа 
1. `id` integer [primary key]
1. `user_id` integer [not null]
1. `task_id` integer [not null]
1. `role_id` integer [not null]

# Tasks - задачи
1. `id` integer [primary key]
1. `time_limit` integer [not null] - между 250 ms и 15000 ms
1. `memory_limit` integer [not null] - между 4 MB и 1024 MB
1. `created_at` timestamp [default: `now()`] - время создания задачи
1. `name` text
1. `legend` text
1. `input` text
1. `output` text
1. `generation_rules` text - правила генерации тестов (пока не придумал)

# Tags - пользовательские теги к задачам
1. `id` integer [primary key]
1. `task_id` integer [not null]
1. `tag` text [not null]

# Tests - тесты
1. `id` integer [primary key]
1. `task_id` integer [not null]
1. `source_id` integer [not null]
1. `generator_id` integer
1. `input` text [not null]
1. `output` text [not null]
1. `is_valid` bool [not null] - буду считать тест валидным, если он прошел валидатор + чекер на всех правильных решениях

# Generators - кастомные пользовательские генераторы тестов
1. `id` integer [primary key]
1. `owner_id` integer [not null]
1. `language_id` integer [not null]
1. `generators_types_id` integer [not null]
1. `code` text [not null] - код из файла с расширение *.cpp
1. `modified_date` timestamp [not null, default: `now()`] - последнее время изменения генератора

# Solutions - решения задач
1. `id` integer [primary key]
1. `task_id` integer  [not null]
1. `owner_id` integer [not null]
1. `language_id` integer [not null]
1. `solutions_types_id` integer [not null]
1. `code` text [not null] - код из файла с расширение *.cpp
1. `modified_date` timestamp [not null, default: `now()`] - последнее время изменения генератора

# Checkers - пользовательские программы, которые проверяют корректность ответов на тестах. Может быть только 1 для задачи.
1. `id` integer [primary key]
1. `owner_id` integer [not null]
1. `task_id` integer [not null]
1. `language_id` integer [not null]
1. `code` text [not null] - код из файла с расширение *.cpp

# Validators - пользовательская программа, которая проверяет корректность теста. Может быть только 1 для задачи.
1. `id` integer [primary key]
1. `owner_id` integer [not null]
1. `task_id` integer [not null]
1. `language_id` integer [not null]
1. `code` text [not null] - код из файла с расширение *.cpp

# Runs - результат запуска теста на каком-то решении
1. `id` integer [primary key]
1. `test_id` integer [not null]
1. `solution_id` integer [not null]
1. `output` text [not null]
1. `time` integer [not null] - затраченное время в ms
1. `memory` integer [not null] - затраченная память в MB
1. `verdict_id` integer [not null]

# Roles - роли пользователей при sharing
1. `id` integer [primary key]
1. `role` enum [not null, unique] (Owner, Editor, Viewer)

# Verdicts - вердикта запуска задач на тестах
1. `id` integer [primary key]
1. `verdict` enum [not null, unique] (Accepted, Wrong answer, Runtime error, Time limit exceeded, Memory limit exceeded,
Running, Presentation Error)

# Languages - языки программирования
1. `id` integer [primary key]
1. `language` enum [not null, unique] (В mvp только C++20)

# Sources - источник теста
1. `id` integer [primary key]
1. `sources` enum [not null, unique] (Manual, From user generator, From our system)

# Generators types - тип генератора
1. `id` integer [primary key]
1. `type` enum [not null, unique] (Correct, Incorrect)

# Solutions types - типы решений
1. `id` integer [primary key]
2. `type` enum [not null, unique] (Correct, Incorrect, Wrong answer, Time limit, Memory limit)
