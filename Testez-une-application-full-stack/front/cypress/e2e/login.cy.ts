describe('Login spec', () => {
  const loginUrl = '/login';

  const typeAndSubmit = (email: string, password: string) => {
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
  };

  const assertInvalidForm = () => {
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('input[formControlName=password]').should(
      'have.class',
      'ng-invalid'
    );
    cy.get('.error').should('contain.text', 'An error occurred');
    cy.get('button[type="submit"]').should('be.disabled');
  };

  const assertValidForm = () => {
    cy.get('input[formControlName=email]').should('have.class', 'ng-valid');
    cy.get('input[formControlName=password]').should('have.class', 'ng-valid');
    cy.get('button[type="submit"]').should('not.be.disabled');
  };

  const loginSuccessTest = () => {
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

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    typeAndSubmit('yoga@studio.com', 'test!1234');

    cy.url().should('include', '/sessions');
  };

  const invalidLoginFormTest = () => {
    cy.visit(loginUrl);

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Invalid Credentials' },
    });

    typeAndSubmit('te', '');
    assertInvalidForm();

    typeAndSubmit('sting', '');
    assertInvalidForm();

    typeAndSubmit('@test.com', 'test');
    assertValidForm();
  };

  const loginFailureTest = () => {
    cy.visit(loginUrl);

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Invalid Credentials' },
    });

    typeAndSubmit('yoga@studio.com', 'test!1234');

    cy.get('.error').should('contain.text', 'An error occurred');
    cy.url().should('include', loginUrl);
  };

  const apiLoginCallDataCheck = () => {
    cy.visit(loginUrl);

    cy.intercept('POST', '/api/auth/login', {
      body: {},
    }).as('loginInterception');

    typeAndSubmit('yoga@studio.com', 'test!1234');

    cy.wait('@loginInterception').then((interception) => {
      expect(interception.request.body).to.have.property(
        'email',
        'yoga@studio.com'
      );
      expect(interception.request.body).to.have.property(
        'password',
        'test!1234'
      );
    });
  };

  it('Correct Datas in API call', apiLoginCallDataCheck);
  it('Login successful', loginSuccessTest);
  it('Invalid login form', invalidLoginFormTest);
  it('Login failure', loginFailureTest);
});
