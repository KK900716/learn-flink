@set msg=%date%%time%
@echo %msg%
git add .
git commit -m "%msg%"
git push origin master
@pause