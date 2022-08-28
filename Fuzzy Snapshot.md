Generally to take a snapshot, we'd
- lock the database so that no new updates happen
- take the snapshot of the state *at a particular instant in time*
- unlock the database for changes

For fuzzy snapshot, we do not lock the database for changes.
As a result, the snapshot is take while the changes are being made to the database.

This means => a fuzzy snapshot might not represent data at any particular instant in time.