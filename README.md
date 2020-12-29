# AudioHive

## Docker Compose

Builds up the application connected to [MariaDB](https://mariadb.com/) as database and
[Caddy](https://caddyserver.com/) as reverse proxy HTTPS server.

Requires the following environment variables set:

* `TLS` an email address to use for the ACME account managing ssl certificates or `"internal"` to use
* `DOMAIN` hostname for the server

To test locally you could run `TLS=internal DOMAIN=localhost docker-compose up --build`

## Server setup for deployment via GitLab CI/CD

* Rent Ubuntu 20.04 Server (e.g. Hetzner Cloud CX11)
* Rent a domain name (assuming `audiohive.example` here), point DNS `A` entries to the server
* generate a keypair and copy it to the server
    ```shell
    ssh-keygen -t rsa -f audiohive-xyz-rsa # no passphrase!
    ssh-copy-id -i audiohive-xyz-rsa root@audiohive.example
    ```

* in GitLab `Project settings -> CI -> Variables` add
  - Key: `PROD_SSH_KEY`

    Type: File

    Value: content of `audiohive-xyz-rsa`

    Protected: Yes

  - Key: `PROD_DOMAIN`

    Type: Variable

    Value: `audiohive.example`

  - Key: `PROD_TLS_EMAIL`

    Type: Variable

    Value: your email address

  - Key: `PROD_HOST_KEYS`

    Type: File

    Value: output of `ssh-keyscan audiohive.example`

  - Key: `PROD_ADMIN_PASSWORD`

    Type: Variable

    Value: a safe password

    Protected: Yes


* install docker

  ```ssh root@audiohive.example```

  and run

    ```shell
    apt-get -y update && \
    apt-get -y install \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg-agent \
        software-properties-common && \
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - && \
    apt-key fingerprint 0EBFCD88 && \
    sudo add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
       $(lsb_release -cs) \
       stable" && \
    apt-get -y update && \
    apt-get -y install docker-ce docker-ce-cli containerd.io
    ```

* ```ssh root@audiohive.example```

  in `/etc/ssh/sshd_config` set `MaxSessions` to 500

  `service sshd restart`
