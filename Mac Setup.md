#MacSetup


```zsh
brew install zsh-syntax-highlighting
brew install mvn
brew install bat
brew install alacritty
brew install visual-studio-code

cat >> ~/.zsh_aliases <<- EOM
alias cat='bat'
alias dps='docker ps -a --format \"table {{.Names}}\\\t{{.Status}}\\\t{{.Ports}}\"'
EOM

cat >> ~/.zshenv <<- EOM
export CDPATH="$HOME":"$HOME/Workspace"
EOM

cat >> ~/.zshrc <<- EOM
setopt auto_cd
source $HOME/.zsh_aliases
EOM

cat >> ~/.vimrc <<- EOM
syntax on
set number
set ruler
set autoindent
filetype plugin indent on
set tabstop=2
set shiftwidth=2
set expandtab
set relativenumber
EOM


```


