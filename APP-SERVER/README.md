# VLUP Server

## Setup do ambiente

Lembre de sempre usar python3

Instale Docker no seu sistema, e tenha certeza de que pode rodar o comando `docker` sem sudo:

http://askubuntu.com/questions/477551/how-can-i-use-docker-without-sudo

### Ambiente de Dev

- Instale no Sublime: `SublimeLinter e SublimeLinter-flake8`
- Instalação de ambiente de dev: `sudo apt-get install flake8 python3-flake8 && pip3 install flake8`
- Coloque no seu settings do Sublime: `"folder_exclude_patterns": [".svn", ".git", ".hg", "CVS", "__pycache__", "*.egg-info"]`

### Rodando containers

- Execute (DEV):

`docker-compose -f dev.yaml build`
`docker-compose -f dev.yaml up`

- Execute (PROD):

`docker-compose build`
`docker-compose up`