(function () {
        const endPoins = 'ios/games/charts/';
        const host = window.location + endPoins;

        let btnUpdate = document.querySelector('#btn-update');
        btnUpdate.addEventListener('click', btnUpdateClick);

        function btnUpdateClick() {
            let objTypeGames = getCheckboxValues('#checkbox-contener-type-games');
            if (Object.keys(objTypeGames).length == 0) return alert('Select at least one category of games!');
            let inputLimitForm = document.querySelector('#input-limit-list');
            sessionStorage.setItem('limit', +inputLimitForm.value);
            for (let key in objTypeGames) {
                sessionStorage.setItem(key, objTypeGames[key]);
            }
            updateGamesList();
        }

        function updateGamesList() {

            if (sessionStorage.length === 0) {
                sessionStorage.setItem('check1', 'free');
                sessionStorage.setItem('check2', 'paid');
                sessionStorage.setItem('check3', 'grossing');
                sessionStorage.setItem('limit', 10);
            }
            let listTypeGames = getlistTypeGames();
            let limitList = getLimitList();
            getGamesFromServer(listTypeGames, limitList);
        };

        function getlistTypeGames() {
            let listTypeGames = [];
            let nameCheckBox = 'check';
            for (let i = 0; i < sessionStorage.length; i++) {
                let valueOfkey = sessionStorage.getItem(nameCheckBox + i);
                if (valueOfkey !== '' && valueOfkey !== null) listTypeGames.push(valueOfkey);
            }
            return listTypeGames;
        }

        function getLimitList() {
            let valueLimit = +sessionStorage.getItem('limit');
            if (valueLimit > 0) return valueLimit;
            return 0;
        }

        function getCheckboxValues(idCheckboxContener) {
            let checkboxContener = document.querySelector(idCheckboxContener);
            let listCheckbox = checkboxContener.querySelectorAll('input[type="checkbox"]');
            let objTypeGames = {};
            let count = 0;
            listCheckbox.forEach(element => {
                if (element.checked) objTypeGames[element.name] = element.value;
                else {
                    count++;
                    objTypeGames[element.name] = '';
                }
            });
            if (count === listCheckbox.length) return {};
            return objTypeGames;
        }

        function getGamesFromServer(listTypeGames, limitLengthList) {
            clearListGames();
            listTypeGames.forEach(el => {
                showHeadListGames(el);
                let url = new URL(host + el);
                if (limitLengthList !== 0) {
                    url.search = new URLSearchParams({limit: limitLengthList});
                }
                fetch(url)
                    .then(resp => {
                        if (resp.ok) {
                            return resp.json();
                        }
                        else throw resp;
                    })
                    .then(data => {
                        showListGames(el, data)
                    })
                    .catch(err => {
                        if (err.text) {
                            err.text().then(text => alert(text));
                        } else {
                            alert(err);
                        }
                    });
            });
        }

        function clearListGames() {
            let tableBody = document.querySelector('#table-list-games');
            let listDeleleted = tableBody.querySelectorAll('tbody');
            for (let i = 0; i < listDeleleted.length; i++) {
                listDeleleted[i].remove();
            }
        }

        function showHeadListGames(typeGames) {
            let tableBody = document.querySelector('#table-list-games');

            let templNameListGames = document.querySelector('#tr-type-games-templ');
            let cloneTempl = document.importNode(templNameListGames.content, true);
            let tdName = cloneTempl.querySelector('.td-type-games');
            tdName.textContent = typeGames[0].toUpperCase() + typeGames.slice(1);
            tdName.classList.add('td-type-games-color-' + typeGames);

            let tbodyTempl = cloneTempl.querySelector('tbody');
            tbodyTempl.id = 'tbody-list-' + typeGames;
            tableBody.appendChild(cloneTempl);
        }


        function showListGames(typeGames, listGames) {
            let elementForInsert = document.querySelector('#tbody-list-' + typeGames);

            listGames.forEach(itemGame => {
                let templIteamListGames = document.querySelector('#tr-item-games-templ');
                let cloneItemTempl = document.importNode(templIteamListGames.content, true);

                let listFieldTr = cloneItemTempl.querySelectorAll('td');
                let index = 0;
                for (const key in itemGame) {
                    if (key === 'url') {
                        let a = document.createElement('a');
                        a.href = itemGame[key];
                        a.textContent = itemGame[key];
                        a.setAttribute('target', '_blank');
                        listFieldTr[index].appendChild(a);
                    } else {
                        listFieldTr[index].textContent = itemGame[key];
                    }
                    listFieldTr[index].classList.add('td-color-' + typeGames);
                    index++;
                }
                elementForInsert.appendChild(cloneItemTempl);
            });
        }

        updateGamesList();
    }

)();