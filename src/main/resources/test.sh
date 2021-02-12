sh ~/.bashrc
alias readgen >/dev/null 2>&1 && echo "Not found" || echo "\nalias readgen='java -jar ~/.readgen/readgen.main.jar'" >> ~/.bashrc
