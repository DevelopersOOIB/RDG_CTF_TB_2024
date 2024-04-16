<?php
class User implements Serializable {
    private $username;
    private $password;
    private $isAdmin;

    public function __construct($username, $password, $isAdmin = false) {
        $this->username = $username;
        $this->password = $password;
        $this->isAdmin = $isAdmin;
    }

    public function serialize() {
        return serialize([
            'username' => $this->username,
            'password' => $this->password,
            'isAdmin' => $this->isAdmin,
        ]);
    }

    public function unserialize($serialized) {
        $data = unserialize($serialized);
        $this->username = $data['username'];
        $this->password = $data['password'];
        $this->isAdmin = $data['isAdmin'];
    }

    public function getUsername() {
        return $this->username;
    }

    public function getPassword() {
        return $this->password;
    }

    public function isAdmin() {
        return $this->isAdmin;
    }
}
?>
