# dbdiagram.io
```dbml
Table users { 
  id integer [primary key]
  login text [not null, unique]
  name text [not null]
  hashed_password text [not null]
}

Table sharing {
   id integer [primary key]
   user_id integer [not null]
   task_id integer [not null]
   role_id integer [not null]
}

Table tasks {
  id integer [primary key]
  time_limit integer [not null]
  memory_limit integer [not null]
  created_at timestamp [default: `now()`]
  name text 
  legend text
  input text
  output text
  generation_rules text
}

Table tags {
  id integer [primary key]
  task_id integer [not null]
  tag text [not null]
}

Table tests {
  id integer [primary key]
  task_id integer [not null]
  source_id integer [not null]
  generator_id integer
  input text [not null]
  output text [not null]
  is_valid bool [not null]
}

Table generators {
  id integer [primary key]
  owner_id integer [not null]
  language_id integer [not null]
  generators_types_id integer [not null]
  code text [not null]
  modified_date timestamp [not null, default: `now()`]
}

Table solutions {
  id integer [primary key]
  task_id integer  [not null]
  owner_id integer [not null]
  language_id integer [not null]
  solutions_types_id integer [not null]
  code text [not null]
  modified_date timestamp [not null, default: `now()`]
}

Table checkers {
  id integer [primary key]
  owner_id integer [not null]
  task_id integer [not null]
  language_id integer [not null]
  code text [not null]
}


Table validators {
  id integer [primary key]
  owner_id integer [not null]
  task_id integer [not null]
  language_id integer [not null]
  code text [not null]
}


Table runs {
  id integer [primary key]
  test_id integer [not null]
  solution_id integer [not null]
  output text [not null]
  time integer [not null]
  memory integer [not null]
  verdict_id integer [not null]
}

// dictionary
Table roles {
  id integer [primary key]
  role enum [not null, unique]
}

Table verdicts {
   id integer [primary key]
   verdict enum [not null, unique]
}

Table languages {
  id integer [primary key]
  language enum [not null, unique]
}

Table sources {
  id integer [primary key]
  sources enum [not null, unique]
}

Table generators_types {
   id integer [primary key]
   type enum [not null, unique]
}

Table solutions_types {
   id integer [primary key]
   type enum [not null, unique]
}


Ref: sharing.role_id > roles.id
Ref: sharing.user_id > users.id
Ref: sharing.task_id > tasks.id

Ref: tags.task_id > tasks.id

Ref: tests.source_id > sources.id
Ref: tests.task_id > tasks.id
Ref: tests.generator_id > generators.id
Ref: generators.generators_types_id > generators_types.id
Ref: generators.owner_id > users.id
Ref: generators.language_id > languages.id

Ref: runs.test_id > tests.id
Ref: runs.solution_id > solutions.id
Ref: runs.verdict_id > verdicts.id

Ref: solutions.solutions_types_id > solutions_types.id

Ref: solutions.task_id > tasks.id
Ref: solutions.owner_id > users.id
Ref: solutions.language_id > languages.id

Ref: validators.owner_id > users.id
Ref: validators.task_id > tasks.id
Ref: validators.language_id > languages.id

Ref: checkers.owner_id > users.id
Ref: checkers.task_id > tasks.id
Ref: checkers.language_id > languages.id
```