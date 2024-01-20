describe('Me / Account spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCall');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';

  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  const connectAsAdmin = () => {
    cy.visit(loginUrl);

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    login();
    cy.url().should('include', sessionUrl);
  };

  const connectAsNotAdmin = () => {
    cy.visit(loginUrl);

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
      },
    });

    login();
    cy.url().should('include', sessionUrl);
  };

  const checkUserDataDisplay = () => {
    cy.contains('p', 'Name: firstName LASTNAME').should('exist');
    cy.contains('p', 'Email: email@test.com').should('exist');
    cy.contains('p', 'Create at: January 1, 2024').should('exist');
    cy.contains('p', 'Last update: January 1, 2024').should('exist');
  };

  const adminGetAdminMessageDisplay = () => {
    cy.contains('p.my2', 'You are admin').should('exist');

    cy.contains('p', 'Delete my account:').should('not.exist');
    cy.contains('button.mat-raised-button.mat-warn', 'Detail').should(
      'not.exist'
    );
  };

  const nonAdminGetDeleteAccountButton = () => {
    cy.contains('p', 'Delete my account:').should('exist');
    cy.contains('button.mat-raised-button.mat-warn', 'Detail').should('exist');

    cy.contains('p.my2', 'You are admin').should('not.exist');
  };

  const accountPageViewOnAdmin = () => {
    cy.intercept('get', '/api/user/1', {
      body: {
        id: 1,
        email: 'email@test.com',
        lastName: 'lastName',
        firstName: 'firstName',
        admin: true,
        password: 'password',
        createdAt: '2024-01-01',
        updatedAt: '2024-01-01',
      },
    });

    connectAsAdmin();
    cy.contains('span.link[routerlink="me"]', 'Account').click();
  };

  const accountPageViewOnNotAdmin = () => {
    cy.intercept('get', '/api/user/1', {
      body: {
        id: 1,
        email: 'email@test.com',
        lastName: 'lastName',
        firstName: 'firstName',
        admin: false,
        password: 'password',
        createdAt: '2024-01-01',
        updatedAt: '2024-01-01',
      },
    });

    connectAsNotAdmin();
    cy.contains('span.link[routerlink="me"]', 'Account').click();
  };

  const shouldHaveCorrectUserDataDisplayOnAdmin = () => {
    accountPageViewOnAdmin();
    checkUserDataDisplay();
    adminGetAdminMessageDisplay();
  };

  const shouldHaveCorrectUserDataDisplayOnNotAdmin = () => {
    accountPageViewOnNotAdmin();
    checkUserDataDisplay();
    nonAdminGetDeleteAccountButton();
  };

  it('View account page on Admin', accountPageViewOnAdmin);
  it('View account page on Not Admin', accountPageViewOnNotAdmin);
  it(
    'should have admin user data display',
    shouldHaveCorrectUserDataDisplayOnAdmin
  );
  it(
    'should have non admin user data display',
    shouldHaveCorrectUserDataDisplayOnNotAdmin
  );
});
