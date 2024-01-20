describe('Routing spec', () => {
  const sessionsUrl = '/sessions';
  const loginUrl = '/login';

  it('should redirect on login page for a non connected user', () => {
    cy.visit(sessionsUrl);

    cy.url().should('include', loginUrl);
  });
});
