# $1 server
# $2 database name
# $3 restore point
# $4 database user
# $5 database password
echo "restoring db name $2 on server $1 from restore point '$3'"
"sqlcmd.exe" -S $1  -U $4 -P $5 -Q "RESTORE DATABASE $2 from DISK='$3'"