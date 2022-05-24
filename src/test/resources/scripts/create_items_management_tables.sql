CREATE TABLE IF NOT EXISTS items_management.items
(
  `id`   bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS items_management.operations
(
  `id`      bigint NOT NULL AUTO_INCREMENT,
  `amount`  int NOT NULL,
  `type`    varchar(255) NOT NULL,
  `item_id` bigint,
PRIMARY KEY(id)
);
