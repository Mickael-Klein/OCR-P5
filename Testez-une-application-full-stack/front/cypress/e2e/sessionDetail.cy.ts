describe('Sessions Detail spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCallAll');

    cy.intercept('GET', '/api/session/1', {
      fixture: 'session.json',
    }).as('sessionsApiCallOne');

    cy.intercept('GET', '/api/teacher/10', {
      fixture: 'teacher.json',
    }).as('teacherApiCallOne');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';
  const sessionDetailUrl = '/sessions/detail/1';

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

  const navigateOnSessionDetailPage = () => {
    cy.get('mat-card.item')
      .first()
      .find('button.mat-raised-button')
      .first()
      .click();

    cy.url().should('include', sessionDetailUrl);
  };

  const sessionInfoCheck = () => {
    cy.contains('h1', 'Session 1').should('exist');
    cy.contains('span.ml1', 'firstName LASTNAME').should('exist');
    cy.contains('span.ml1', '3 attendees').should('exist');
    cy.contains('span.ml1', 'January 25, 2024').should('exist');
    cy.contains('div.description', 'Prepare yourself').should('exist');
    cy.contains('div.created', ' January 25, 2024').should('exist');
    cy.contains('div.updated', ' January 25, 2024').should('exist');
  };

  const sessionDetailPageViewOnAdmin = () => {
    connectAsAdmin();
    navigateOnSessionDetailPage();
    cy.contains('span.ml1', 'Delete').should('exist');
    cy.contains('span.ml1', 'Participate').should('not.exist');

    sessionInfoCheck();
  };

  const sessionDetailPageViewOnNotAdmin = () => {
    connectAsNotAdmin();
    navigateOnSessionDetailPage();
    cy.contains('span.ml1', 'Delete').should('not.exist');
    cy.contains('span.ml1', 'Participate').should('exist');

    sessionInfoCheck();
  };

  it('View Session detail page on Admin', sessionDetailPageViewOnAdmin);
  it('View Session detail page on Not Admin', sessionDetailPageViewOnNotAdmin);
});
