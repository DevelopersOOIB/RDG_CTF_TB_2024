<?php
// Удаляем cookie с пользователем
setcookie('user', '', time() - 3600, '/');

// Перенаправляем на страницу входа
header("Location: index.php");
exit();
?>
