TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJPcmFuZ2VKdWljZSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdfQ.Ej57HP5RR-DylDZlEloSBn2y7qf7usja6ao-lgXkgxo"

for i in {1..30}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
    -H "Authorization: Bearer $TOKEN" \
    http://localhost:8080/api/v1/books/1 &
done
wait

echo "Done!"
read -p "Press enter to exit"